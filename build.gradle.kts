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
    implementation("dev.kord:kord-core:0.9.x-SNAPSHOT")
    implementation("dev.kord.x:kordx.emoji:feature-mpp-SNAPSHOT")
    implementation("com.squareup.okio:okio-nodefilesystem:3.3.0")
    implementation("org.jetbrains.kotlin-wrappers", "kotlin-node", "v18.15.11-pre.538")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.5.0")
}

kotlin {
    js(IR) {
        nodejs {
            binaries.executable()
        }
    }
}
