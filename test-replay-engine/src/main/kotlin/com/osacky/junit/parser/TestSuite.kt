package com.osacky.junit.parser

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper

data class TestSuite(
    val name: String,
    private val tests: Int,
    private val failures: Int,
    private val errors: Int,
    private val skipped: Int,
    private val time: Float,
    private val timestamp: String,
    private val hostname: String?,
    private val disabled: Int?,
    private val testLabExecutionId: String?,
    private val properties: String?,
    @JsonProperty("testcase")
    @JacksonXmlElementWrapper(useWrapping = false)
    val testCases: List<TestCase>
)