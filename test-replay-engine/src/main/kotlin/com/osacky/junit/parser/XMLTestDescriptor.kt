package com.osacky.junit.parser

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import java.io.File
import java.lang.RuntimeException

class XMLTestDescriptor(uniqueId: UniqueId, displayName: String, private val reportFile: File) : AbstractTestDescriptor(uniqueId, displayName) {

  init {
    val testSuite = XmlMapper()
        .registerKotlinModule()
        .readValue(reportFile.readText(), TestSuite::class.java)
    testSuite.testCases.forEach {
      addChild(TestCaseTestDescriptor(uniqueId.append("test", it.name), it.classname + "#" + it.name, failure = it.failure))
    }
  }

  override fun getType(): TestDescriptor.Type {
    return TestDescriptor.Type.CONTAINER
  }
}