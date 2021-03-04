package com.osacky.junit.parser

import org.junit.platform.engine.*
import java.io.File
import java.io.FilenameFilter

class WSEngine : TestEngine {

  val RESULT_FILE_KEY = "resultFile"
  val RESULT_DIR_KEY = "resultDir"

  override fun getId(): String {
    return "result-parsing-engine"
  }

  override fun discover(discoveryRequest: EngineDiscoveryRequest, uniqueId: UniqueId): TestDescriptor {
    return XMLTestDescriptor(uniqueId, id, determineFile())
  }

  override fun execute(request: ExecutionRequest) {
    request.rootTestDescriptor.accept {
      if (it is TestCaseTestDescriptor) {
        request.engineExecutionListener.executionStarted(it)
        if (it.isFailure) {
          // TODO change to stacktrace
          request.engineExecutionListener.executionFinished(
            it,
            TestExecutionResult.failed(IllegalStateException(it.failure?.message))
          )
        } else {
          request.engineExecutionListener.executionFinished(it, TestExecutionResult.successful())
        }
      } else if (it is TestClassTestDescriptor) {
      }
    }
  }

  private fun determineFile() : File {
    val resultFile = System.getProperty(RESULT_FILE_KEY)
    if (resultFile != null) {
      return File(resultFile)
    }
    val resultDir = System.getProperty(RESULT_DIR_KEY)
    if (resultDir != null) {
      val xmlFilter = FilenameFilter { _, name -> name.endsWith(".xml") }
      val files = File(resultDir).listFiles(xmlFilter)
      if (files.isNullOrEmpty()) {
        throw IllegalArgumentException("Found no xml files in $resultDir")
      }
      if (files.size != 1) {
        throw IllegalArgumentException("Found ${files.size} in $resultDir. I only know how to parse 1.")
      }
      return files.single()
    }
    throw IllegalArgumentException("Neither $RESULT_DIR_KEY nor $RESULT_FILE_KEY system properties were set")
  }
}