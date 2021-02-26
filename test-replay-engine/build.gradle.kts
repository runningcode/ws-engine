plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.31"

    id("maven-publish")
}

group = "com.osacky.ws.engine"
version = "1.0"

sourceSets {
    create("intTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val intTestImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

val intTestRuntimeOnly = configurations["intTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

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
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.12.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.1")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("org.junit.platform:junit-platform-testkit")
    testImplementation("com.google.truth:truth:1.1.2")

    intTestImplementation(gradleTestKit())
    intTestImplementation("com.google.truth:truth:1.1.2")
    intTestImplementation("org.jetbrains.kotlin:kotlin-test-junit")

}

val projectMaven = file("$buildDir/localMaven")

val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["intTest"].output.classesDirs
    classpath = sourceSets["intTest"].runtimeClasspath
    shouldRunAfter("test")
    systemProperty("local.repo", projectMaven)
    dependsOn("publishMavenPublicationToMavenRepository")
}

tasks.check { dependsOn(integrationTest) }

publishing {
    repositories {
        maven {
            url = projectMaven.toURI()
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
