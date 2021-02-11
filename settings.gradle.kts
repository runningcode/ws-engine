plugins {
  id("com.gradle.enterprise").version("3.5.2")
}

rootProject.name = "ws-engine"
include("test-replay-engine")
include("integ-test")

gradleEnterprise {
  buildScan {
    server = "https://ge-unstable.grdev.net"
  }
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}