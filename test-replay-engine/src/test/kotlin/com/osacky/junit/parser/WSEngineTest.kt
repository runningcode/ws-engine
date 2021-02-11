package com.osacky.junit.parser

import org.assertj.core.api.Condition
import org.junit.Test
import org.junit.platform.engine.TestExecutionResult
import org.junit.platform.launcher.LauncherDiscoveryRequest
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder
import org.junit.platform.testkit.engine.EngineExecutionResults
import org.junit.platform.testkit.engine.EngineTestKit

class WSEngineTest {

  @Test
  fun testResultParsingEngine() {
    val xml = "/Users/no/workspace/test-framework/test-replay-engine/src/test/resources/junit-sample-report-no-error.xml"
    System.setProperty("resultFile", xml)
    val results = execute(
      LauncherDiscoveryRequestBuilder.request()
        .build()
    )
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
    val xml = "/Users/no/workspace/test-framework/test-replay-engine/src/test/resources/junit-sample-report.xml"
    System.setProperty("resultFile", xml)
    val results = execute(
      LauncherDiscoveryRequestBuilder.request()
        .build()
    )
    results.allEvents()
      .assertEventsMatchLooselyInOrder(
        Condition(
          { event -> event.testDescriptor.displayName == "com.example.app.ExampleUiTest#test3" },
          "matches test1"
        ),
        Condition({ event ->
          event.testDescriptor.displayName == "com.example.app.TestFoo#test2"
              && (event.payload.get() as TestExecutionResult).status.name == "FAILED"
        }, "matches test2")
      )
  }

  private fun execute(request: LauncherDiscoveryRequest): EngineExecutionResults {
    return EngineTestKit.execute(WSEngine(), request)
  }
}