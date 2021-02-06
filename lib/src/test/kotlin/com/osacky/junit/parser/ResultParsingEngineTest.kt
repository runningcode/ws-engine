package com.osacky.junit.parser

import org.assertj.core.api.Condition
import org.junit.Test
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.launcher.LauncherDiscoveryRequest
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.testkit.engine.EngineExecutionResults
import org.junit.platform.testkit.engine.EngineTestKit
import org.junit.platform.testkit.engine.Event
import java.io.File

class ResultParsingEngineTest {

  @Test
  fun testResultParsingEngine() {
    val xml = "/Users/no/workspace/test-framework/lib/src/test/resources/junit-sample-report-no-error.xml"
    val results = execute(LauncherDiscoveryRequestBuilder.request().build(), xml)
    results.allEvents()
      .assertEventsMatchLooselyInOrder(
        Condition(
          { event -> event.testDescriptor.displayName == "com.example.app.ExampleUiTest#test3" },
          "matches test1"
        ),
        Condition({ event -> event.testDescriptor.displayName == "com.example.app.TestFoo#test2" }, "matches test2")
      )
  }

  @Test
  fun canParseTestFailure() {
    val xml = "/Users/no/workspace/test-framework/lib/src/test/resources/junit-sample-report.xml"
    val results = execute(LauncherDiscoveryRequestBuilder.request().build(), xml)
    results.allEvents()
      .assertEventsMatchLooselyInOrder(
        Condition(
          { event -> event.testDescriptor.displayName == "com.example.app.ExampleUiTest#test3" },
          "matches test1"
        ),
        Condition({ event -> event.testDescriptor.displayName == "com.example.app.TestFoo#test2"
            && (event.payload.get() as TestExecutionResult).status.name == "FAILED" }, "matches test2")
      )
  }

  private fun execute(request: LauncherDiscoveryRequest, dir: String): EngineExecutionResults {
    return EngineTestKit.execute(ResultParsingEngine(dir), request)
  }
}