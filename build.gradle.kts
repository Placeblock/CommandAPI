group = "de.placeblock"
version = "2.2.0-SNAPSHOT"
description = "API for an easier use of Commands"
var isReleaseVersion = !version.toString().endsWith("SNAPSHOT")

plugins {
    `java-library`
    id("maven-publish")
    id("signing")
}

java {
    withJavadocJar()
    withSourcesJar()
    // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

signing {
    sign(publishing.publications)
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
        title = "BetterInventories API Documentation"
    }
}

repositories {
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("io.github.waterfallmc:waterfall-api:1.20-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-api:4.11.0")
    compileOnly("net.kyori:adventure-platform-bungeecord:4.3.0")

    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    testCompileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    testCompileOnly("io.github.waterfallmc:waterfall-api:1.20-R0.1-SNAPSHOT")
    testImplementation("net.kyori:adventure-api:4.11.0")

    testCompileOnly("org.projectlombok:lombok:1.18.26")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.26")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.junit-pioneer:junit-pioneer:2.0.0")
    testImplementation("org.mockito:mockito-core:5.2.0")
}



publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "CommandAPI"
            version = project.version.toString()
            from(components["java"])
            pom {
                packaging = "jar"
                name.set("BetterInventories")
                description.set("Easy to use and extensive InventoryAPI for Spigot")
                url.set("https://github.com/Placeblock/BetterInventories")
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
                        "https://github.com/Placeblock/BetterInventories.git"
                    )
                    connection.set(
                        "scm:git:git://github.com/Placeblock/BetterInventories.git"
                    )
                    developerConnection.set(
                        "scm:git:git://github.com/Placeblock/BetterInventories.git"
                    )
                }
                issueManagement {
                    url.set("https://github.com/Placeblock/BetterInventories/issues")
                }
            }
        }
    }
    repositories {
        maven {
            name = "mavenCentral" + if (isReleaseVersion) "Release" else "Snapshot"
            url = uri(
                if (isReleaseVersion) "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                else "https://s01.oss.sonatype.org/content/repositories/snapshots"
            )
            credentials {
                username = project.properties["ossrh.username"] as String?
                password = project.properties["ossrh.password"] as String?
            }
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}
