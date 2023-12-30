package dev.schlaubi.alphabet

import dev.kord.x.emoji.DiscordEmoji

actual fun findCountryFlag(name: String): DiscordEmoji? = find(name.lowercase())?.let {
    DiscordEmoji.Generic(it.emoji)
}
