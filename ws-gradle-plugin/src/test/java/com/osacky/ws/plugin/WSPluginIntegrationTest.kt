package com.osacky.ws.plugin

import com.google.common.truth.Truth.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class WSPluginIntegrationTest {

  @get:Rule
  val testProjectRoot = TemporaryFolder()

  @Test
  fun testPluginCanRunTests() {

    testProjectRoot.writeBuildGradle("""
      plugins {
        id "com.osacky.wiedersehen"
      }
      
      repositories {
        maven {
          url = "${System.getProperty("local.repo")}"
        }
        mavenCentral()
      }
      wsExtension {
        junitXMLFile = 'junit-sample-report-no-error.xml'
      }
      tasks.withType(Test).configureEach {
        testLogging {
          events "passed", "skipped", "failed"
        }
      }
      
    """.trimIndent())

    val file = File(testProjectRoot.root, "junit-sample-report-no-error.xml")
    testProjectRoot.copyJunitXml("junit-sample-report-no-error.xml", file)


    val result = GradleRunner.create()
      .withPluginClasspath()
      .withProjectDir(testProjectRoot.root)
      .withArguments("wsTest", "--init-script", System.getProperty("initGradle"))
      .build()

    assertThat(result.output).contains("BUILD SUCCESSFUL")
    assertThat(result.task(":wsTest")!!.outcome).isEqualTo(TaskOutcome.SUCCESS)
    assertThat(result.output.contains("""
      UnknownClass.com.example.app.ExampleUiTest#test3 PASSED

      UnknownClass.com.example.app.TestFoo#test2 PASSED
    """.trimIndent()))
  }
}