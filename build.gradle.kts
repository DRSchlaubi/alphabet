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
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

kotlin {
    jvm()
    js(IR) {
        nodejs {
            binaries.executable()
        }
    }

    mingwX64()
    macosArm64()
    macosX64()
    linuxX64()
//    linuxArm64()

    targets.withType<KotlinNativeTarget> {
        binaries.executable {
            entryPoint = "dev.schlaubi.alphabet.launcher.main"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kord.core)
                implementation(libs.kordx.emoji)
                implementation(libs.kotlinx.io)
                implementation(libs.kotlinx.serialization.json.io)
                implementation(libs.kotlinx.datetime)
            }
        }

        named("jsMain") {
            dependencies {
                implementation(libs.nodejs)
            }
        }
    }
}
