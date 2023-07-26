package dev.schlaubi.alphabet

import node.process.process
import okio.FileSystem
import okio.NodeJsFileSystem

actual val fs: FileSystem = NodeJsFileSystem
actual fun getEnv(name: String): String? = process.env[name]
