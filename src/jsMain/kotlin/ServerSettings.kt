@file:OptIn(ExperimentalSerializationApi::class)

package dev.schlaubi.alphabet

import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.GuildBehavior
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.okio.decodeFromBufferedSource
import kotlinx.serialization.json.okio.encodeToBufferedSink
import okio.NodeJsFileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

private val locks = mutableMapOf<Snowflake, Mutex>()
private val fs = NodeJsFileSystem
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

private fun GuildBehavior.toLocation() = "servers/$id.json".toPath()

suspend fun GuildBehavior.retrieveSettings() = cache[id] ?: fsRetrieveSettings()

private suspend fun GuildBehavior.fsRetrieveSettings() = withLock(this) {
    val path = toLocation()
    if (!fs.exists(path)) return@withLock ServerSettings()
    fs.source(toLocation()).use {
        it.buffer().use { buffer ->
            json.decodeFromBufferedSource<ServerSettings>(buffer)
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
        it.buffer().use { buffer ->
            json.encodeToBufferedSink(settings, buffer)
        }
    }
    cache[id] = settings
}

private suspend inline fun <R : Any> withLock(guild: GuildBehavior, action: () -> R): R {
    val mutex = locks.getOrPut(guild.id) { Mutex() }

    return mutex.withLock(action = action)
}
