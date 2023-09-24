plugins {
    id("maven-publish")
    id("signing")
}

description = "API for an easier use of Commands"
version = "2.2.7"

dependencies {
    compileOnly("net.kyori:adventure-api:4.14.0")
    testImplementation("net.kyori:adventure-api:4.14.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.release.set(17)
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    test {
        useJUnitPlatform()
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        title = "CommandAPI API Documentation"
    }
}

tasks.withType<PublishToMavenRepository>() {
    doFirst {
        println("Publishing ${publication.groupId}:${publication.artifactId}:${publication.version} to ${repository.url}")
        publication.artifacts.forEach {
            println("Classifier ${it.classifier}")
            println("File ${it.file}")
            println("Extension ${it.extension}")
        }
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
                name.set("CommandAPI")
                description.set("Easy to use CommandAPI")
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
