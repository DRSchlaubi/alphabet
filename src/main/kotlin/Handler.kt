package dev.schlaubi.alphabet

import dev.kord.common.annotation.KordExperimental
import dev.kord.common.annotation.KordUnsafe
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.GuildBehavior
import dev.kord.core.behavior.reply
import dev.kord.core.cache.data.RemovedReactionData
import dev.kord.core.entity.ReactionEmoji
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private val locks = mutableMapOf<Snowflake, Mutex>()

@OptIn(KordUnsafe::class, KordExperimental::class)
fun Kord.installBaseHandler() = on<MessageCreateEvent> {
    val safeGuildId = guildId ?: return@on
    val guild = kord.unsafe.guild(safeGuildId)
    val settings = guild.retrieveSettings()
    if (message.author?.isBot != false || message.channel.id != settings.channel) return@on
    val received = message.content.uppercase()
    if (!received.all(Char::isLetter)) return@on
    val isHighScore = withLock(guild) {
        if (settings.lastUser == message.author?.id || received != settings.currentChar.next
        ) {
            message.reply {
                content = "${message.author?.mention} is an idiot and ruined it at `${settings.currentChar.next}`"
            }
            guild.saveSettings(settings.copy(currentChar = startingChar, lastUser = null, currentScore = 0))
            return@on
        }

        val newScore = settings.currentScore + 1
        val highScore = newScore.takeIf { newScore > settings.highScore } ?: settings.highScore

        guild.saveSettings(
            settings.copy(
                currentChar = received,
                currentScore = newScore,
                lastUser = message.author?.id,
                highScore = highScore
            )
        )

        highScore != settings.highScore
    }

    if (isHighScore) {
        message.addReaction(ReactionEmoji.from(RemovedReactionData(name = "☑\uFE0F")))
    } else {
        message.addReaction(ReactionEmoji.from(RemovedReactionData(name = "✅")))
    }
}

val Char.next
    get() = (code + 1).toChar()

val String?.next get() = this?.next ?: "A"

val String.next: String
    get() = nextFunction(this)

private val nextFunction = DeepRecursiveFunction<String, String> {
    with(it) {
        val last = lastOrNull() ?: return@DeepRecursiveFunction "A"
        if (last != 'Z') {
            dropLast(1) + last.next
        } else {
            callRecursive(dropLast(1)) + 'A'
        }
    }
}

private suspend inline fun <R : Any> withLock(guild: GuildBehavior, action: () -> R): R {
    val mutex = locks.getOrPut(guild.id) { Mutex() }

    return mutex.withLock(action = action)
}
