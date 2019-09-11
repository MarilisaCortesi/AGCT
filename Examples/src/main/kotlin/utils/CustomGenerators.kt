package utils

import agct.AlchemistGenerator
import model.circuit.GeneticCircuit

class ExportableAlchemist(private val exportBlock: GeneticCircuit.() -> String) : AlchemistGenerator() {
    override val files: GeneticCircuit.() -> Map<String, String> = {
        super.files(this).mapValues { "${it.value}\n\n${exportBlock()}" }
    }
}