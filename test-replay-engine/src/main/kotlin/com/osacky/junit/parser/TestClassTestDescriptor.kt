package com.osacky.junit.parser

import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor

class TestClassTestDescriptor(uniqueId: UniqueId, displayName: String, suite: TestSuite) : AbstractTestDescriptor(uniqueId, displayName) {
  init {
    suite.testCases.forEach {
      addChild(TestCaseTestDescriptor(
        uniqueId.append("test", it.name),
        it.classname + "#" + it.name,
        failure = it.failure
      ))
    }
  }

  val SEGMENT_TYPE = "class";
  override fun getType(): TestDescriptor.Type {
    return TestDescriptor.Type.CONTAINER
  }
}