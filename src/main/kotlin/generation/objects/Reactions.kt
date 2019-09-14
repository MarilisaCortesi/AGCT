@file:Suppress("PackageDirectoryMismatch", "ClassName")

package agct

import generation.AbstractGenerator
import generation.defaultDirectory
import model.circuit.GeneticCircuit

object reactions : ReactionsGenerator({ "$defaultDirectory/reactions.txt" })

open class ReactionsGenerator(filename: GeneticCircuit.() -> String) : AbstractGenerator({ file ->
    file[filename()] =
        reactions.map { it.reactions }.flatten().joinToString("\n", "${name.toUpperCase()}\n\n")
})