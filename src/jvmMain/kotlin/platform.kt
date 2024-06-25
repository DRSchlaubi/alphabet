package dev.schlaubi.alphabet

actual fun getEnv(name: String): String? = System.getenv(name)
