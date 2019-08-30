package export.types

import export.ExportType
import export.utils.toFile
import model.circuit.GeneticCircuit

internal class Reactions : ExportType {
    override fun from(circuit: GeneticCircuit) = with(circuit) {
        circuit.reactions
            .map { it.reactions }
            .flatten()
            .joinToString("\n", "${name.toUpperCase()}\n\n")
            .toFile("reactions.txt", circuit.name)
    }
}