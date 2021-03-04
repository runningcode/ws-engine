package com.osacky.junit.parser

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.platform.engine.TestDescriptor
import org.junit.platform.engine.UniqueId
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor
import java.io.File

class XMLTestDescriptor(uniqueId: UniqueId, displayName: String, reportFile: File) : AbstractTestDescriptor(uniqueId, displayName) {

  init {
    val reportText = reportFile.readText()
    val testSuites = try {
      TestSuites(0, 0, 0, 0, 0.0f, listOf(XmlMapper()
        .registerKotlinModule()
        .readValue(reportText, TestSuite::class.java)))
    } catch (e : MissingKotlinParameterException) {
      XmlMapper()
        .registerKotlinModule()
        .readValue(reportText, TestSuites::class.java)
    }
    testSuites.testSuite.forEach { testSuite ->
      addChild(
        TestClassTestDescriptor(uniqueId.append("class", testSuite.name), testSuite.name , testSuite
      ))
    }
  }

  override fun getType(): TestDescriptor.Type {
    return TestDescriptor.Type.CONTAINER
  }
}