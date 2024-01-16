plugins {
    id("maven-publish")
    id("signing")
    `java-library`
}

group = "de.codelix"
description = "Paper support for the CommandAPI"
version = "4.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}
dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    api(project(":adventure"))
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks["jar"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
            version = project.version.toString()
            pom {
                packaging = "jar"
                name.set("CommandAPI-Paper")
                description.set(project.description)
                url.set("https://github.com/Placeblock/CommandAPI")
                licenses {
                    license {
                        name.set("GNU General Public License, Version 3")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.html")
                    }
                }
                developers {
                    developer {
                        name.set("Felix")
                        organization.set("Codelix")
                        organizationUrl.set("https://codelix.de/")
                    }
                }
                scm {
                    url.set(
                        "https://github.com/Placeblock/CommandAPI.git"
                    )
                    connection.set(
                        "scm:git:git://github.com/Placeblock/CommandAPI.git"
                    )
                    developerConnection.set(
                        "scm:git:git://github.com/Placeblock/CommandAPI.git"
                    )
                }
                issueManagement {
                    url.set("https://github.com/Placeblock/CommandAPI/issues")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications)
}
