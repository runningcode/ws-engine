package com.osacky.ws.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.create
import java.io.File

class WSPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {

      val extension = extensions.create<WSExtension>("wsExtension")
      val testRuntimeOnly = configurations.create("testRuntimeOnly")

      dependencies.add("testRuntimeOnly", "com.osacky.ws:test-replay-engine:1.0-SNAPSHOT")

      tasks.register("wsTest", Test::class.java) {
        doFirst {
          project.writeDummyClassFile()
        }

        useJUnitPlatform {
          includeEngines("result-parsing-engine")
        }
        outputs.upToDateWhen { false }

        inputs.file(extension.junitXMLFile)
          .withPathSensitivity(PathSensitivity.RELATIVE)
          .withPropertyName("junitXMLFile")
          .skipWhenEmpty()

        classpath = files(testRuntimeOnly, file("$buildDir/ws-engine"))

        testClassesDirs = files(file("$buildDir/ws-engine"))
        // TODO make configurable in extension
        systemProperty("resultFile", project.file(extension.junitXMLFile.get()))

        binaryResultsDirectory.set(file("${buildDir}/reports/ws-engine/binary"))
        reports.html.isEnabled = false
        reports.junitXml.isEnabled = false
      }
    }
  }

  /**
   * Writes a dummy class file so that the test task will call the ws-test-engine.
   */
  fun Project.writeDummyClassFile() {
    WSPlugin::class.java.classLoader.getResourceAsStream("Foo.class")!!.use { inputStream ->
      val wsEngine = File("$buildDir/ws-engine/com/example")
      wsEngine.mkdirs()
      File(wsEngine, "Foo.class").outputStream().use { outStream ->
        inputStream.copyTo(outStream)
      }
    }
  }
}