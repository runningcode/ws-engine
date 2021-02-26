plugins {
  id("com.gradle.enterprise").version("3.5.2")
}

rootProject.name = "ws-engine"
include("test-replay-engine")
include("integ-test")
include("no-plugin-integ")

gradleEnterprise {
  buildScan {
//    server = "https://ge-unstable.grdev.net"
//    publishAlways()
  }
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}