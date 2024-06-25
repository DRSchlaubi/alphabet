@file:OptIn(ExperimentalSerializationApi::class, ExperimentalSerializationApi::class,
    ExperimentalSerializationApi::class
)

package dev.schlaubi.alphabet

import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.GuildBehavior
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.io.decodeFromSource
import kotlinx.serialization.json.io.encodeToSink

private val locks = mutableMapOf<Snowflake, Mutex>()
internal val fs = SystemFileSystem
private val json = Json
private val cache = mutableMapOf<Snowflake, ServerSettings>()
val startingChar = null

@Serializable
data class ServerSettings(
    val channel: Snowflake? = null,
    val highScore: Int = 0,
    val currentScore: Int = 0,
    val currentChar: String? = startingChar,
    val lastUser: Snowflake? = null
)

private fun GuildBehavior.toLocation() = Path("servers/$id.json")

suspend fun GuildBehavior.retrieveSettings() = cache[id] ?: fsRetrieveSettings()

private suspend fun GuildBehavior.fsRetrieveSettings() = withLock(this) {
    val path = toLocation()
    if (!fs.exists(path)) return@withLock ServerSettings()
    fs.source(toLocation()).use {
        it.buffered().use { buffer ->
            json.decodeFromSource<ServerSettings>(buffer)
        }
    }
}

suspend fun GuildBehavior.saveSettings(settings: ServerSettings) = withLock(this) {
    val path = toLocation()
    val parent = path.parent ?: error("Parent was null for some reason")
    if (!fs.exists(parent)) {
        fs.createDirectories(parent)
    }
    fs.sink(toLocation()).use {
        it.buffered().use { buffer ->
            json.encodeToSink(settings, buffer)
        }
    }
    cache[id] = settings
}

private suspend inline fun <R : Any> withLock(guild: GuildBehavior, action: () -> R): R {
    val mutex = locks.getOrPut(guild.id) { Mutex() }

    return mutex.withLock(action = action)
}
