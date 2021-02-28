import com.gradle.enterprise.gradleplugin.GradleEnterprisePlugin
import com.gradle.scan.plugin.BuildScanPlugin
import org.gradle.util.GradleVersion

initscript {
  val pluginVersion = "3.5.2"

  repositories {
    gradlePluginPortal()
  }
  dependencies {
    classpath("com.gradle:gradle-enterprise-gradle-plugin:${pluginVersion}")
  }
}

val isTopLevelBuild = gradle.getParent() == null

if (isTopLevelBuild) {
  val gradleVersion = GradleVersion.current().baseVersion
  val atLeastGradle5 = gradleVersion >= GradleVersion.version("5.0")
  val atLeastGradle6 = gradleVersion >= GradleVersion.version("6.0")

  if (atLeastGradle6) {
    beforeSettings {
      if (!pluginManager.hasPlugin("com.gradle.enterprise")) {
        pluginManager.apply(GradleEnterprisePlugin::class)
      }
      configureExtension(extensions["gradleEnterprise"])
    }
  } else if (atLeastGradle5) {
    rootProject {
      pluginManager.apply(BuildScanPlugin::class)
      configureExtension(extensions["gradleEnterprise"])
    }
  }
}

fun configureExtension(extension: Any) {
  extension.withGroovyBuilder {
    getProperty("buildScan").withGroovyBuilder {
      "publishAlways"()
      setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
      setProperty("termsOfServiceAgree", "yes")
    }
  }
}