
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
        url = uri("http://schark.io:8085/private")
        isAllowInsecureProtocol = true
        credentials {
            username = project.properties["reposilite.username"] as String?
            password = project.properties["reposilite.token"] as String?
        }
    }
    mavenCentral()
}

dependencies {
  compileOnly("org.projectlombok:lombok:1.18.24")
  implementation("io.schark:ScharkDesign:1.0.3")
  implementation("net.kyori:adventure-api:4.11.0")
  implementation("net.kyori:adventure-text-minimessage:4.11.0")
  annotationProcessor("org.projectlombok:lombok:1.18.24")
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "de.placeblock"
            artifactId = "CommandAPI"
            version = "1.0.0"

            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "ScharkIO"
            url = uri("http://schark.io:8085/private")
            isAllowInsecureProtocol = true
            credentials{
                username = project.properties["reposilite.username"] as String?
                password = project.properties["reposilite.token"] as String?
            }
        }
    }
}
