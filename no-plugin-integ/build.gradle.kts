val testRuntimeOnly = configurations.create("testRuntimeOnly")

dependencies {
  testRuntimeOnly(project(":test-replay-engine"))
}

tasks.register<Test>("integTest") {
  useJUnitPlatform {
    includeEngines("result-parsing-engine")
  }
  outputs.upToDateWhen { false }

  classpath = files(testRuntimeOnly + file("$rootDir/integ-test/build/classes/java/test"))
  testClassesDirs = files(file("$rootDir/integ-test/build/classes/java/test"))
  systemProperty("resultFile", file("src/test/resources/junit-sample-report.xml"))

  binaryResultsDirectory.set(file("${buildDir}/reports/replay/binary"))
  reports.html.isEnabled = false
  reports.junitXml.isEnabled = false
}