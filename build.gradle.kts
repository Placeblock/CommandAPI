
group = "de.placeblock.CommandAPI"
version = "1.0.0"
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
  compileOnly("org.projectlombok:lombok:1.18.24")
  compileOnly("io.schark:ScharkDesign:1.0.3")
  compileOnly("net.kyori:adventure-platform-bungeecord:4.1.1")
  annotationProcessor("org.projectlombok:lombok:1.18.24")
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "de.placeblock"
            artifactId = "CommandAPI"
            version = "1.1.1"

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
