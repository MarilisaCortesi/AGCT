@file:Suppress("PackageDirectoryMismatch", "ClassName")

package agct

import io.github.agct.generation.Generator

object export

object each

object into {
    operator fun invoke(vararg generators: Generator) =
        generators.toSet()
}