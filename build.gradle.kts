
group = "de.placeblock"
version = "1.2.2"
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
    testCompileOnly("org.projectlombok:lombok:1.18.24")
    compileOnly("io.schark:ScharkDesign:1.3.3")
    compileOnly("net.kyori:adventure-platform-bungeecord:4.1.2")
    testCompileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    testCompileOnly("io.github.waterfallmc:waterfall-api:1.18-R0.1-SNAPSHOT")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
  annotationProcessor("org.projectlombok:lombok:1.18.24")
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
