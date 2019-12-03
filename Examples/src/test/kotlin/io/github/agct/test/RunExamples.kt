package io.github.agct.test

import io.github.classgraph.ClassGraph
import io.kotlintest.specs.StringSpec

internal class RunExamples : StringSpec({
    ClassGraph().whitelistPackages("io.github.agct.examples")
        .enableAllInfo()
        .scan()
        .allClasses
        .map { it to it.getMethodInfo().find { it.isStatic && it.name == "main" } }
        .filterNot { it.second == null }
        .forEach { (clazz, main) ->
            "Example in ${clazz} should not crash" {
                main!!.loadClassAndGetMethod().invoke(null)
            }
        }
})