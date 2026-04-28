plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation(projects.alphabet)
}

application {
    mainClass = "dev.schlaubi.alphabet.launcher.MainKt"
}
