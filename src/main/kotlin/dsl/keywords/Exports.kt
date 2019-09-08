@file:Suppress("PackageDirectoryMismatch", "ClassName")

package dsl

import generation.Generator

object export

object each

object into {
    operator fun invoke(vararg generators: Generator) =
        generators.toSet()
}