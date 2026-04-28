@file:JvmName("ContentProcessingCommon")

package dev.schlaubi.alphabet

import dev.kord.x.emoji.DiscordEmoji
import dev.kord.x.emoji.Emojis
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.accept
import org.intellij.markdown.ast.acceptChildren
import org.intellij.markdown.ast.visitors.RecursiveVisitor
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import kotlin.jvm.JvmName

private val flavour = CommonMarkFlavourDescriptor()
private val parser = MarkdownParser(flavour)

private val plainTextTypes: List<IElementType> = listOf(
    MarkdownTokenTypes.WHITE_SPACE,
    MarkdownTokenTypes.TEXT,
    MarkdownTokenTypes.EOL,
    MarkdownTokenTypes.CODE_LINE,
    MarkdownTokenTypes.CODE_FENCE_CONTENT,
    MarkdownTokenTypes.ESCAPED_BACKTICKS
)

private class OnlyBoldVisitor(private val source: String, private val result: StringBuilder) : RecursiveVisitor() {
    private inner class StrongVisitor : RecursiveVisitor() {
        override fun visitNode(node: ASTNode) {
            if (node.type in plainTextTypes) {
                val text = source.substring(node.startOffset, node.endOffset)
                result.append(text)
            }
            super.visitNode(node)
        }
    }

    override fun visitNode(node: ASTNode) {
        if (node.type == MarkdownElementTypes.STRONG) {
            node.acceptChildren(StrongVisitor())
        }
        super.visitNode(node)
    }
}

fun String.extractBoldText(): String = buildString {
    val markdown = parser.buildMarkdownTreeFromString(this@extractBoldText)
    markdown.accept(OnlyBoldVisitor(this@extractBoldText, this))
}.ifBlank { this }

fun findCountryFlag(name: String): DiscordEmoji? = DiscordEmoji.findByNameOrNull(name)

fun String.findEmoji(isHighScore: Boolean): DiscordEmoji {
    return findCountryFlag(this)
        ?: if (isHighScore) Emojis.ballotBoxWithCheck else Emojis.whiteCheckMark
}
