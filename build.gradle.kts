plugins {
    kotlin("js") version "1.8.20"
    kotlin("plugin.serialization") version "1.8.20"
}

group = "dev.schlaubi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation("dev.kord", "kord-core", "0.10.0-SNAPSHOT")
    implementation("com.squareup.okio", "okio-nodefilesystem", "3.3.0")
    implementation("org.jetbrains.kotlin-wrappers", "kotlin-node", "18.16.12-pre.583")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.5.0")
}

kotlin {
    js(IR) {
        nodejs {
            binaries.executable()
        }
    }
}
