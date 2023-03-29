group = "de.placeblock"
version = "2.1.5a"
description = "API for an easier use of Commands"

plugins {
  `java-library`
  id("maven-publish")
}

java {
  // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    maven {
        url = uri("https://repo.schark.io/private")
        isAllowInsecureProtocol = true
        credentials {
            username = project.properties["reposilite.username"] as String?
            password = project.properties["reposilite.token"] as String?
        }
    }
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("io.github.waterfallmc:waterfall-api:1.18-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.26")
    compileOnly("io.schark:ScharkDesign:1.6.0")
    compileOnly("net.kyori:adventure-platform-bungeecord:4.3.0")
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    testCompileOnly("org.projectlombok:lombok:1.18.26")
    testCompileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    testCompileOnly("io.github.waterfallmc:waterfall-api:1.18-R0.1-SNAPSHOT")
    testImplementation("net.kyori:adventure-platform-bungeecord:4.3.0")
    testImplementation("net.kyori:adventure-text-minimessage:4.13.0")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.26")
    testImplementation("io.schark:ScharkDesign:1.6.0")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.junit-pioneer:junit-pioneer:2.0.0")
    testImplementation("org.mockito:mockito-core:5.2.0")
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String?
            artifactId = project.name
            version = project.version as String?

            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "ScharkIO"
            url = uri("https://repo.schark.io/private")
            isAllowInsecureProtocol = true
            credentials{
                username = project.properties["reposilite.username"] as String?
                password = project.properties["reposilite.token"] as String?
            }
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}
