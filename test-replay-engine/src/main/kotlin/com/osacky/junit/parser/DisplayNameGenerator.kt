package com.osacky.junit.parser

class DisplayNameGenerator {
  fun forPackageClassString(name : String) : String {
    val lastDot: /*@@dpiqeu@@*/Int = name.lastIndexOf('.')
    return name.substring(lastDot + 1)
  }
}