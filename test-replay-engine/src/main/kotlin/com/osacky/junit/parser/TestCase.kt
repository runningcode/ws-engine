package com.osacky.junit.parser

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

data class TestCase (
  val name: String,
  val classname: String,
  private val time: Float?,
  val failure: TestFailure?
)

data class TestFailure(
  val type: String,
  val message: String,
  ) {
  @get:JacksonXmlText
  lateinit var stacktrace: String
}