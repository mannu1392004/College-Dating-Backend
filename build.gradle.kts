plugins {
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
	id("org.springframework.boot") version "3.4.0-M1"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
	implementation("org.springframework.boot:spring-boot-starter-mail")

	implementation(kotlin("stdlib-jdk8"))
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// mongo db dependency
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

	// websocket
	implementation("org.springframework.boot:spring-boot-starter-websocket")

	// s3 bucket
	implementation("aws.sdk.kotlin:s3:1.3.27")
	implementation("com.amazonaws:aws-java-sdk-s3:1.12.552")


}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// Custom task to build and run the application
tasks.register<JavaExec>("buildAndRun") {
	group = "application"
	description = "Builds and runs the Spring Boot application"
	dependsOn("build")
	mainClass.set("com.example.CollegeDateApplication") // Replace with your main class
	classpath = sourceSets["main"].runtimeClasspath
}
