package com.osacky.ws.plugin

import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.property


open class WSExtension(objects: ObjectFactory) {
  val junitXMLFile = objects.property<String>()
}