package com.osacky.junit.parser

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class WSEngineIntegrationTest {

  @get:Rule
  val testProjectRoot = TemporaryFolder()

  @Test
  fun testCanUseResultFile() {
    EngineSampleProject(testProjectRoot, """
      <?xml version='1.0' encoding='UTF-8' ?>
      <testsuite name="" tests="1" failures="0" errors="0" skipped="0" time="2.278" timestamp="2018-09-14T20:45:55" hostname="localhost" testLabExecutionId="matrix-1234_execution-asdf">
          <properties />
          <testcase name="test3" classname="com.example.app.ExampleUiTest" time="0.328" />
          <testcase name="test2" classname="com.example.app.TestFoo"/>
      </testsuite>
    """.trimIndent()).writeProject()

    val result = GradleRunner.create()
      .withProjectDir(testProjectRoot.root)
      .withArguments("check")
      .build()

    assert(result.task(":test")!!.outcome!! == TaskOutcome.SUCCESS)
  }

  @Test
  fun testFailure() {
    EngineSampleProject(testProjectRoot, """
     <?xml version='1.0' encoding='UTF-8' ?>
      <testsuite name="" tests="1" failures="0" errors="0" skipped="0" time="2.278" timestamp="2018-09-14T20:45:55" hostname="localhost" testLabExecutionId="matrix-1234_execution-asdf">
          <properties />
          <testcase name="test3" classname="com.example.app.ExampleUiTest" time="0.328" />
          <testcase name="test2" classname="com.example.app.TestFoo">
            <failure message="java.lang.IllegalStateException: NotEnoughFoo" type="java.lang.IllegalStateException">
              this is the stack trace
            </failure>
          </testcase>
      </testsuite>
    """.trimIndent()).writeProject()

    val result = GradleRunner.create()
      .withProjectDir(testProjectRoot.root)
      .withArguments("check")
      .buildAndFail()

    assert(result.task(":test")!!.outcome!! == TaskOutcome.FAILED)
    assert(result.output.contains("There were failing tests"))
  }

}