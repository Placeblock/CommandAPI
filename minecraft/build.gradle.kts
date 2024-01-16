plugins {
    id("java")
}

group = "de.codelix"
description = "API for an easier use of Commands"
version = "4.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    compileOnly(project(":core"))

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
}
java {
    withJavadocJar()
    withSourcesJar()
    // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.release.set(17)
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        title = "CommandAPI API Documentation"
    }
}
