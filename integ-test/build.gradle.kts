plugins {
  `java-library`
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
  outputs.upToDateWhen { false }

  systemProperties = mapOf("resultFile" to file("src/test/resources/junit-sample-report.xml"))
}
