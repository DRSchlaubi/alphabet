package dev.schlaubi.alphabet

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import okio.FileSystem
import platform.posix.getenv

actual val fs: FileSystem = FileSystem.SYSTEM
@OptIn(ExperimentalForeignApi::class)
actual fun getEnv(name: String): String? = getenv(name)?.toKString()


