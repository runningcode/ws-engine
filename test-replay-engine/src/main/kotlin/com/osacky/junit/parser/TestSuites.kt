package com.osacky.junit.parser

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

data class TestSuites(
  val disabled: Int,
  val errors: Int,
  val failures: Int,
  val tests: Int,
  val time: Float,
  @JsonProperty("testsuite")
  @JacksonXmlElementWrapper(useWrapping = false)
  val testSuite: List<TestSuite>
) {
}