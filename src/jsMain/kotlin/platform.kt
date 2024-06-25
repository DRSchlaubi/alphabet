package dev.schlaubi.alphabet

import node.process.process

actual fun getEnv(name: String): String? = process.env[name]
