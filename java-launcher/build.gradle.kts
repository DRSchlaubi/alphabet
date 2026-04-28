plugins {
    application
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(projects.alphabet)
}

application {
    mainClass = "dev.schlaubi.alphabet.launcher.MainKt"
}
