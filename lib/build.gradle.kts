plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.4.30"

    // Apply the java-library plugin for API and implementation separation.
    `java-library`
}

repositories {
    mavenCentral()
}

tasks.test {
//    project.files("foo").filter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation(platform("org.junit:junit-bom:5.7.0")) {
        because("Platform, Jupiter, and Vintage versions should match")
    }

    implementation("org.junit.platform:junit-platform-engine")

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("org.junit.platform:junit-platform-testkit")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.11.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.3")

}
