@file:Suppress("PackageDirectoryMismatch", "ClassName")

package agct

import generation.AbstractGenerator
import generation.defaultDirectory
import model.circuit.GeneticCircuit

object entities : EntitiesGenerator({ "$defaultDirectory/entities.txt" })

open class EntitiesGenerator(filename: GeneticCircuit.() -> String) : AbstractGenerator( { file ->
    file[filename()] =
        entities.joinToString("\n", "${name.toUpperCase()}\n\n")
})