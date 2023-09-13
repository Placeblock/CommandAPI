plugins {
    id("maven-publish")
    id("signing")
}

version = "2.2.4"

repositories {
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly(project(":core"))
    implementation(project(":minecraft"))
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.release.set(17)
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
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
                name.set("Paper Bridge")
                description.set("Paper Bridge for CommandAPI")
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
