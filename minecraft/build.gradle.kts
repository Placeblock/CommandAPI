
version = "1.0.0"

dependencies {
    compileOnly(project(":core"))

    compileOnly("net.kyori:adventure-api:4.14.0")
    testImplementation("net.kyori:adventure-api:4.14.0")
}
