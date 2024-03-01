import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockStoreTask
import org.jetbrains.kotlin.konan.target.Family

plugins {
    application
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "dev.schlaubi"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://europe-west3-maven.pkg.dev/mik-music/kord")
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

    mingwX64()
    macosArm64()
    macosX64()
    linuxX64()
    linuxArm64()

    targets.withType<KotlinNativeTarget> {
        binaries.executable {
            entryPoint = "dev.schlaubi.alphabet.launcher.main"
            if (konanTarget.family == Family.LINUX) {
                linkerOpts("--as-needed", "--defsym=isnan=isnan")
                freeCompilerArgs =
                    freeCompilerArgs + listOf("-Xoverride-konan-properties=linkerGccFlags=-lgcc -lgcc_eh -lc")
            }
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
