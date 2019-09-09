package generation

import model.circuit.GeneticCircuit

interface Generator {
    fun from(circuit: GeneticCircuit)
}

fun GeneticCircuit.exportTo(vararg generators: Generator) {
    checkRules()

    for (generator in generators) {
        generator.from(this)
    }
}