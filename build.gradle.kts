import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockStoreTask

plugins {
    kotlin("multiplatform") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
}

group = "dev.schlaubi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

kotlin {
    js(IR) {
        nodejs {
            binaries.executable()
        }
    }

    sourceSets {
        named("jsMain") {
            dependencies {
                implementation("dev.kord:kord-core:0.10.0-SNAPSHOT")
                implementation("dev.kord.x:kordx.emoji:feature-mpp-SNAPSHOT")
                implementation("com.squareup.okio:okio-nodefilesystem:3.4.0")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-node:18.16.12-pre.594")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-okio:1.5.1")
            }
        }
    }
}

tasks {
    named<YarnLockStoreTask>("kotlinStoreYarnLock") {
        yarnLockMismatchReport = provider { YarnLockMismatchReport.WARNING }
    }
}
