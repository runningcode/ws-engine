package com.osacky.ws.plugin

import org.junit.rules.TemporaryFolder
import java.io.File

fun TemporaryFolder.writeBuildGradle(build: String) {
    writeFileToName("build.gradle", build)
}

fun TemporaryFolder.writeFileToName(fileName: String, contents: String) {
    newFile(fileName).writeText(contents)
}

fun TemporaryFolder.copyJunitXml(fixtureName: String, destination: File) {
    File(this::class.java.classLoader.getResource(fixtureName)!!.file).copyTo(destination)
}
