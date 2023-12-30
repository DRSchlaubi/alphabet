@file:JsNonModule
@file:JsModule("node-emoji")

package dev.schlaubi.alphabet

external fun find(name: String): JsEmoji?

external class JsEmoji {
    val emoji: String
    val name: String
}
