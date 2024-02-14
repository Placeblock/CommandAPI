pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://papermc.io/repo/repository/maven-public/")
  }
}

rootProject.name = "CommandAPI"
include("core")
include("minecraft")
include("paper")
include("adventure")
include("velocity")
