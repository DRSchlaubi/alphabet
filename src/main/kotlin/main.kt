package dev.schlaubi.alphabet

import dev.kord.core.Kord
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import js.core.get
import node.process.process
import okio.NodeJsFileSystem
import okio.Path.Companion.toPath

@OptIn(PrivilegedIntent::class)
suspend fun main() {
    println("Running in: " + process.mainModule?.filename)
    val kord = Kord(process.env["TOKEN"] ?: error("Please specify the token environment variable")).apply {
        registerCommands()
        installBaseHandler()
    }

    kord.login {
        intents += Intent.MessageContent
    }
}
