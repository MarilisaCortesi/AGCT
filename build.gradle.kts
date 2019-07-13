val kotlinTestVersion = "3.3.3"

plugins {
	java
	kotlin("jvm") version "1.3.41"
}

repositories {
	mavenCentral()
}

dependencies {
	compile("org.jetbrains.kotlin:kotlin-scripting-jvm")
	testCompile("junit", "junit", "4.12")
	testCompile("io.kotlintest:kotlintest-runner-junit5:$kotlinTestVersion")
}

tasks {
	withType<Test> {
		useJUnitPlatform()
	}
}

defaultTasks("test")