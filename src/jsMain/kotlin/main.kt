package dev.schlaubi.alphabet

import dev.kord.core.Kord
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import node.process.process

@OptIn(PrivilegedIntent::class)
suspend fun main() {
    val kord = Kord(process.env["TOKEN"] ?: error("Please specify the token environment variable")).apply {
        registerCommands()
        installBaseHandler()
    }

    kord.login {
        intents += Intent.MessageContent
    }
}
