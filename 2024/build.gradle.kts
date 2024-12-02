plugins {
    kotlin("jvm") version "2.1.0"
    application
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

application {
    mainClass = providers.gradleProperty("day").map { "Day${it}Kt" }
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}
