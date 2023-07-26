package dev.schlaubi.alphabet

import dev.kord.core.Kord
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent

expect fun getEnv(name: String): String?

@OptIn(PrivilegedIntent::class)
suspend fun main() {
    val kord = Kord(getEnv("TOKEN") ?: error("Please specify the token environment variable")).apply {
        registerCommands()
        installBaseHandler()
    }

    kord.login {
        intents += Intent.MessageContent
    }
}
