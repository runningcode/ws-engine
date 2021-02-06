package com.osacky.junit.parser

import org.junit.platform.engine.*
import java.io.File

class ResultParsingEngine(private val xmlFile : String) : TestEngine {
  lateinit var resultDescriptor : XMLTestDescriptor

  override fun getId(): String {
    return "result-parsing-engine"
  }

  override fun discover(discoveryRequest: EngineDiscoveryRequest, uniqueId: UniqueId): TestDescriptor {
    resultDescriptor = XMLTestDescriptor(uniqueId, id, File(xmlFile))
    return resultDescriptor
  }

  override fun execute(request: ExecutionRequest) {
    request.rootTestDescriptor.accept {
      if (it is TestCaseTestDescriptor) {
        if (it.isFailure) {
          // TODO change to stacktrace
          request.engineExecutionListener.executionFinished(
            it,
            TestExecutionResult.failed(IllegalStateException(it.failure?.type))
          )
        } else {
          request.engineExecutionListener.executionFinished(it, TestExecutionResult.successful())
        }
      }
//      request.engineExecutionListener.executionFinished(it, TestExecutionResult.successful())
//      }

    }
  }

  fun findInTree(descriptor: TestDescriptor) : TestExecutionResult {
    resultDescriptor.descendants.forEach {
      if (it.uniqueId == descriptor.uniqueId) {
        throw IllegalStateException()
      }
    }
    throw IllegalStateException("Could not find result for $descriptor")
  }
}