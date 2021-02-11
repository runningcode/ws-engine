plugins {
  id("org.jetbrains.kotlin.jvm") version "1.4.30"
}

repositories {
  mavenCentral()
}

dependencies {
  testRuntimeOnly(project(":test-replay-engine"))
}

tasks.withType(Test::class).configureEach {
  useJUnitPlatform {
    includeEngines("result-parsing-engine")
  }

  systemProperties = mapOf("resultFile" to file("src/test/resources/junit-sample-report.xml"))
}
