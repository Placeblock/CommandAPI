plugins {
    id("java")
}

group = "de.codelix"
description = "API for an easier use of Commands"
version = "3.0.0"

repositories {
    mavenCentral()
}
dependencies {
    compileOnly(project(":core"))

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
}
java {
    withJavadocJar()
    withSourcesJar()
    // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
