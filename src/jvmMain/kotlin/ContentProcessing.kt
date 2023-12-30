package dev.schlaubi.alphabet

import dev.kord.x.emoji.DiscordEmoji
import dev.kord.x.emoji.Emojis
import kotlin.reflect.full.declaredMemberProperties

actual fun findCountryFlag(name: String): DiscordEmoji? {
    val fieldName = "flag${name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }}"
    return Emojis::class.declaredMemberProperties.firstOrNull {
        it.name == fieldName
    }?.get(Emojis) as DiscordEmoji?
}
