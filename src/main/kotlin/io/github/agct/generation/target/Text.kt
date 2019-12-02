@file:Suppress("PackageDirectoryMismatch", "ClassName")

package io.github.agct.generation.target

import io.github.agct.generation.AbstractGenerator
import io.github.agct.generation.defaultDirectory
import io.github.agct.model.circuit.GeneticCircuit

object PlainText : TextGenerator({ defaultDirectory })

open class TextGenerator(directory: GeneticCircuit.() -> String) : AbstractGenerator({ file ->
    file["${directory()}/entities.txt"] =
        entities.joinToString("\n", "${name.toUpperCase()}\n\n")

    file["${directory()}/reactions.txt"] =
        reactions.map { it.reactions }.flatten().joinToString("\n", "${name.toUpperCase()}\n\n")
})