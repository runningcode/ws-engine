package com.osacky.junit.parser

import org.junit.rules.TemporaryFolder
import java.io.File

class EngineSampleProject(private val tempRoot : TemporaryFolder, private val junitResultXml: String) {

  private val testSources = tempRoot.newFolder("src", "test", "java")

  fun writeProject() {
    writeBuildDotGradleWithEngine()
    writeEmptyTestJavaSource()
    File(testSources, "results.xml").writeText(junitResultXml)
  }

  private fun writeBuildDotGradleWithEngine() {
    tempRoot.writeBuildGradle("""
      plugins {
        id 'java-library'
      }
      
      repositories {
        mavenCentral()
        maven {
          url = "${System.getProperty("local.repo")}"
        }
      }
      
      tasks.withType(Test).configureEach {
        useJUnitPlatform() {
          includeEngines("result-parsing-engine")
        }
        systemProperties = ["resultFile" : file("src/test/java/results.xml")]
      }
      
      dependencies {
        testRuntimeOnly("com.osacky.ws:test-replay-engine:1.0")
        testRuntimeOnly(platform("org.junit:junit-bom:5.7.0"))
      }
    """.trimIndent())
  }


  // Write empty test java sources in order to skip up to date check
  private fun writeEmptyTestJavaSource() {
    File(testSources, "Foo.java").writeText("""
      class Foo { }
    """.trimIndent())
  }

  private fun TemporaryFolder.writeBuildGradle(build: String) {
    writeFileToName("build.gradle", build)
  }

  private fun TemporaryFolder.writeFileToName(fileName: String, contents: String) {
    newFile(fileName).writeText(contents)
  }
}