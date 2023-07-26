package dev.schlaubi.alphabet

import okio.FileSystem

actual val fs: FileSystem = FileSystem.SYSTEM
actual fun getEnv(name: String): String? = System.getenv(name)
