package com.osacky.junit.parser

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

data class TestCase (
  val name: String,
  val classname: String,
  private val time: Float?,
  val failure: TestFailure?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TestFailure(val type: String)