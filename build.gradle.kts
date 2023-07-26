import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockStoreTask

plugins {
    application
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "dev.schlaubi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

kotlin {
    jvm()
    js(IR) {
        nodejs {
            binaries.executable()
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kord.core)
                implementation(libs.kordx.emoji)
                implementation(libs.okio)
                implementation(libs.kotlinx.serialization.json.okio)
            }
        }

        named("jsMain") {
            dependencies {
                implementation(libs.okio.nodefilesystem)
                implementation(libs.nodejs)
            }
        }
    }
}

application {
    mainClass = "dev.schlaubi.alphabet.MainKt"
}

tasks {
    named<YarnLockStoreTask>("kotlinStoreYarnLock") {
        yarnLockMismatchReport = provider { YarnLockMismatchReport.WARNING }
    }
}
