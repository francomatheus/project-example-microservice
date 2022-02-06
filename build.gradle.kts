import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktlint by configurations.creating

plugins {
	id("org.springframework.boot") version "2.6.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"

	id("io.gitlab.arturbosch.detekt").version("1.19.0")
	id("de.jansauer.printcoverage") version "2.0.0"
	jacoco
}

group = "br.com.project"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11
val jacocoExcludes = listOf("**/generated/**", "**/configuration/**")

repositories {
	mavenCentral()
}
configurations {
	ktlint
}

extra["springCloudVersion"] = "2021.0.0"
extra["testcontainersVersion"] = "1.16.2"

dependencies {
	ktlint("com.pinterest:ktlint:0.43.2") {
		attributes {
			attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
		}
	}
	implementation("io.gitlab.arturbosch.detekt:detekt-formatting:1.19.0")

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
//	implementation("org.springframework.cloud:spring-cloud-bus")
	implementation("org.springframework.cloud:spring-cloud-starter")
	implementation("org.springframework.cloud:spring-cloud-starter-config")

	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
	implementation("io.opentracing.contrib:opentracing-spring-jaeger-web-starter:3.3.1")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mongodb")
}

dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))
val outputFile = "${outputDir}ktlint-checkstyle-report.xml"

val ktlintCheck by tasks.creating(JavaExec::class) {
	inputs.files(inputFiles)
	outputs.dir(outputDir)

	description = "Check Kotlin code style."
	classpath = ktlint
	mainClass.set("com.pinterest.ktlint.Main")
//	args = listOf("src/**/*.kt")
	args = listOf(
		"--reporter=plain",
		"--reporter=checkstyle,output=${outputFile}",
		"src/**/*.kt"
	)
}

val ktlintFormat by tasks.creating(JavaExec::class) {
	inputs.files(inputFiles)
	outputs.dir(outputDir)

	description = "Fix Kotlin code style deviations."
	classpath = ktlint
	mainClass.set("com.pinterest.ktlint.Main")
	args = listOf("-F", "src/**/*.kt")
}


// Configuração Jacoco

val testCoverage by tasks.registering {
	group = "verification"
	description = "Runs the unit tests with coverage."

	dependsOn(":test", ":jacocoTestReport", ":jacocoTestCoverageVerification")
	val jacocoTestReport = tasks.findByName("jacocoTestReport")
	jacocoTestReport?.mustRunAfter(tasks.findByName("test"))
	tasks.findByName("jacocoTestCoverageVerification")?.mustRunAfter(jacocoTestReport)
}

tasks.test {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
	extensions.configure(JacocoTaskExtension::class) {
		excludes = jacocoExcludes
	}
}

tasks.jacocoTestReport {
	reports {
		xml.isEnabled = true
	}
	classDirectories.setFrom(classDirectories.files.map {
		fileTree(it).matching {
			exclude(jacocoExcludes)
		}
	})
	finalizedBy(tasks.printCoverage)
	doLast {
		println("jacocoTestReport")
	}
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				minimum = BigDecimal.valueOf(0.7)
			}
		}
	}
	doLast {
		println("jacocoTestCoverageVerification")
	}
}

tasks.printCoverage {
	finalizedBy(tasks.jacocoTestCoverageVerification)
	doLast {
		println("printCoverage")
	}
}

jacoco {
	toolVersion = "0.8.7"
}

// Configuração Detekt

detekt {
	toolVersion = "1.19.0"
//    config = files("detekt-config.yml")
	buildUponDefaultConfig = false
}
