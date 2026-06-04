plugins {
	java
	id("org.springframework.boot") version "4.0.6"
	id("io.spring.dependency-management") version "1.1.7"
}

val gitTagProvider = providers.exec {
  commandLine("git", "describe", "--tags", "--always", "--dirty")
}

fun getVersionFromGit(): String {
  val tag = gitTagProvider.standardOutput.asText.get().trim()
  val data = tag.split("\\-".toRegex(), 2)
  val parts = data[0].replaceFirst("^v".toRegex(), "").split("\\.".toRegex()).map { it.toInt() }.toIntArray()
  return if (data.size == 1) {
    parts.joinToString(".")
  } else {
    parts[2] += 1
    val newVersion = parts.joinToString(".")
    newVersion + "-SNAPSHOT"
  }
}

group = "me.laurelmay"
version = getVersionFromGit()
description = "game24"

java {
	toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.register("createVersionFile") {
  inputs.property("version", project.version.toString())
  val versionFile = layout.buildDirectory.file("version.txt")
  outputs.file(versionFile)

  doLast {
    versionFile.get().asFile.writeText(project.version.toString())
  }
}

tasks.named("build") {
  dependsOn("createVersionFile")
}
