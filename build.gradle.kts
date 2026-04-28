@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "dev.schlaubi"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        maven("https://europe-west3-maven.pkg.dev/mik-music/kord")
        mavenCentral()
        maven("https://snapshots.kord.dev")
        maven("https://snapshots-repo.kordex.dev")
    }
}

kotlin {
    jvm {
        testRuns.all {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
    }
    js(IR) {
        nodejs {
            binaries.executable()
        }
    }

    mingwX64()
    macosArm64()
    linuxX64()
    linuxArm64()

    targets.withType<KotlinNativeTarget> {
        binaries.executable {
            entryPoint = "dev.schlaubi.alphabet.launcher.main"
        }
    }

    sourceSets {
        dependencies {
            implementation(libs.kord.core)
            implementation(libs.kordx.emoji)
            implementation(libs.kotlinx.io)
            implementation(libs.kotlinx.serialization.json.io)
            implementation(libs.kotlinx.datetime)
            implementation(libs.markdown)

            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }

        jvmTest {
            dependencies {
                implementation(kotlin("test-junit5"))
                runtimeOnly(libs.junit.jupiter.engine)
            }
        }

        jsTest {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        jsMain {
            dependencies {
                implementation(libs.nodejs)
            }
        }
    }
}
