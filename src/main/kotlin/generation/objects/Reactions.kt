@file:Suppress("ClassName")

package generation.objects

import generation.Generator
import generation.utils.toFile
import model.circuit.GeneticCircuit

object reactions : Generator {
    override fun from(circuit: GeneticCircuit) = with(circuit) {
        circuit.reactions
            .map { it.reactions }
            .flatten()
            .joinToString("\n", "${name.toUpperCase()}\n\n")
            .toFile("reactions.txt", circuit.name)
    }
}