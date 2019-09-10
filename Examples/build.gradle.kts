val kotlinTestVersion = "3.3.3"

plugins {
    java
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    compile(rootProject)
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