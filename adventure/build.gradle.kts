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
    compileOnly(project(":minecraft"))
    compileOnly("net.kyori:adventure-api:4.14.0")

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}
java {
    withJavadocJar()
    withSourcesJar()
    // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
