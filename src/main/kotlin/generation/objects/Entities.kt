@file:Suppress("ClassName")

package generation.objects

import generation.Generator
import generation.utils.toFile
import model.circuit.GeneticCircuit

object entities : Generator {
    override fun from(circuit: GeneticCircuit) = with(circuit) {
        entities.joinToString("\n", "${name.toUpperCase()}\n\n")
            .toFile("entities.txt", circuit.name)
    }
}