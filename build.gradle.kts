val kotlinVersion = "1.3.41"
val kotlinTestVersion = "3.3.3"

plugins {
    java
    kotlin("jvm") version "1.3.41"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    compile("org.jetbrains.kotlin:kotlin-scripting-jvm")
    compile("org.slf4j:slf4j-nop:1.7.26")
    testCompile("io.kotlintest:kotlintest-runner-junit5:$kotlinTestVersion")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}

defaultTasks("test")