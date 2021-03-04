plugins {
  `java-gradle-plugin`
  `kotlin-dsl`
  kotlin("jvm")
  id("com.gradle.plugin-publish") version "0.12.0"
  `maven-publish`
}

version = rootProject.version

dependencies {
  compileOnly(gradleApi())
  testImplementation(gradleTestKit())
  testImplementation("junit:junit:4.13.1")
  testImplementation("com.google.truth:truth:1.0.1")
}

gradlePlugin {
  plugins {
    create("ws-gradle-plugin") {
      id = "com.osacky.wiedersehen"
      displayName = "Wiedersehen Plugin"
      description = "See your tests again in Gradle Enterprise"
      implementationClass = "com.osacky.ws.plugin.WSPlugin"
    }
  }
}
val projectMaven = file("${rootProject.buildDir}/localMaven")
val initGradle = file("$rootDir/gradle/gradle-enterprise-init.gradle.kts")
tasks.withType(Test::class).configureEach {
  systemProperty("local.repo", projectMaven)
  systemProperty("initGradle", initGradle)
}