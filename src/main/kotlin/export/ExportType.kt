package export

import model.circuit.GeneticCircuit

internal interface ExportType {
    fun from(circuit: GeneticCircuit)
}