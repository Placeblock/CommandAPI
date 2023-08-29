pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://papermc.io/repo/repository/maven-public/")
  }
}

rootProject.name = "CommandAPI"
include("api")
include("paper")
include("waterfall")
include("bridge")
