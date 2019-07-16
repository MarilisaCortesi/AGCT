package dsl.circuit

import dsl.keywords.export
import model.circuit.GeneticCircuit
import model.circuit.BasicGeneticCircuit

/*
package dsl.circuit

import model.circuit.BasicGeneticCircuit
import model.entities.*
import java.lang.IllegalStateException

class CircuitContext(name: String) {
    val circuit = BasicGeneticCircuit(name)

    fun put(molecule: Molecule) =
        when (molecule) {
            is Gene -> circuit.addGene(molecule)
            is Protein -> circuit.addProtein(molecule)
            is Regulator<*> -> circuit.addRegulator(molecule)
            is RegulativeMolecule -> Unit
            else -> throw IllegalStateException("Unknown molecule class.")
        }
}
*/

class CircuitContext(name: String) : GeneticCircuit by BasicGeneticCircuit(name) {
    infix fun then(export: export) = export.wrapper(this)
}