plugins {
    id("maven-publish")
    id("signing")
    id("java")
}

group = "de.codelix"
description = "API for an easier use of Commands"
version = "3.0.0"

repositories {
    mavenCentral()
}
dependencies {
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

tasks.test {
    useJUnitPlatform()
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.release.set(17)
        options.compilerArgs.add("-parameters")
    }
    compileTestJava {
        options.compilerArgs.add("-parameters")
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        title = "CommandAPI API Documentation"
    }
}

tasks.withType<PublishToMavenRepository> {
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
