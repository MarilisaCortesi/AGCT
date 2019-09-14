@file:Suppress("PackageDirectoryMismatch", "ClassName")

package agct

import generation.AbstractGenerator
import generation.defaultDirectory
import model.circuit.GeneticCircuit

object entities : EntitiesGenerator({ defaultDirectory })

open class EntitiesGenerator(directoryPath: GeneticCircuit.() -> String) : AbstractGenerator( { file ->
    file["${directoryPath()}/entities.txt"] =
        entities.joinToString("\n", "${name.toUpperCase()}\n\n")
})