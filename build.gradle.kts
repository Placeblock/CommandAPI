group = "de.codelix.commandapi"

plugins {
    `java-library`
    id("io.github.gradle-nexus.publish-plugin") version "1.3.0"
}

subprojects {
    group = this.rootProject.group
    apply<JavaLibraryPlugin>()
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
}


nexusPublishing {
    repositories {
        sonatype {  //only for users registered in Sonatype after 24 Feb 2021
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}
