@file:Suppress("PackageDirectoryMismatch", "ClassName")

package agct

import generation.AbstractGenerator
import generation.defaultDirectory
import model.circuit.GeneticCircuit

object PlainText : TextGenerator({ defaultDirectory })

open class TextGenerator(directory: GeneticCircuit.() -> String) : AbstractGenerator({ file ->
    file["${directory()}/entities.txt"] =
        entities.joinToString("\n", "${name.toUpperCase()}\n\n")

    file["${directory()}/reactions.txt"] =
        reactions.map { it.reactions }.flatten().joinToString("\n", "${name.toUpperCase()}\n\n")
})