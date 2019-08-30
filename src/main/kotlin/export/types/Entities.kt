package export.types

import export.ExportType
import export.utils.toFile
import model.circuit.GeneticCircuit
import model.utils.toConsole

internal class Entities : ExportType {
    override fun from(circuit: GeneticCircuit) = with(circuit) {
        entities.joinToString("\n", "${name.toUpperCase()}\n\n")
            .toFile("entities.txt", circuit.name)
    }
}