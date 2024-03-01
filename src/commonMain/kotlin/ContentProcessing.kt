@file:JvmName("ContentProcessingCommon")
package dev.schlaubi.alphabet

import dev.kord.x.emoji.DiscordEmoji
import dev.kord.x.emoji.Emojis
import kotlin.jvm.JvmName

fun String.extractBoldText(): String {
    val pattern = Regex("""\*\*(.*?)\*\*""")
    val matches = pattern.findAll(this).toList()
    // If there are no bold marking use entire string instead
    if (matches.isEmpty()) return this
    return matches.joinToString("") { it.groupValues[1] }
}

fun findCountryFlag(name: String): DiscordEmoji? = DiscordEmoji.findByNameOrNull(name)

fun String.findEmoji(isHighScore: Boolean): DiscordEmoji {
    return findCountryFlag(this)
        ?: if(isHighScore) Emojis.ballotBoxWithCheck else Emojis.whiteCheckMark
}
