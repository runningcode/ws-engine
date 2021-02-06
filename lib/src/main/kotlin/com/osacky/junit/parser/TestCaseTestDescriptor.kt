package com.osacky.junit.parser

import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor

class TestCaseTestDescriptor(uniqueId: UniqueId, displayName: String, val failure: TestFailure?) : AbstractTestDescriptor(uniqueId, displayName) {
  override fun getType(): TestDescriptor.Type {
    return TestDescriptor.Type.TEST
  }

  val isFailure : Boolean = failure != null
}