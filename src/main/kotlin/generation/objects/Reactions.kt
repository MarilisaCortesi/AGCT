@file:Suppress("PackageDirectoryMismatch", "ClassName")

package agct

import generation.AbstractGenerator
import generation.defaultDirectory
import model.circuit.GeneticCircuit

object reactions : ReactionsGenerator({ defaultDirectory })

open class ReactionsGenerator(directoryPath: GeneticCircuit.() -> String) : AbstractGenerator({ file ->
    file["${directoryPath()}/reactions.txt"] =
        reactions.map { it.reactions }.flatten().joinToString("\n", "${name.toUpperCase()}\n\n")
})