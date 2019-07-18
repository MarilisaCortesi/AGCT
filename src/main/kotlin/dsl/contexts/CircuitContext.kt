package dsl.contexts

import dsl.keywords.export
import model.circuit.GeneticCircuit
import model.circuit.BasicGeneticCircuit
import model.entities.*
import java.lang.IllegalArgumentException

class CircuitContext internal constructor(name: String) : GeneticCircuit by BasicGeneticCircuit(name) {
    /**
     * The wrapped circuit.
     */
    internal val circuit = BasicGeneticCircuit(name)

    /**
     * Checks if a molecule exists, if not returns null, otherwise returns it only if it has type T.
     *
     * @param id the molecule id
     * @param T the type of the molecule
     */
    internal inline fun<reified T : Molecule> getAndCheck(id: String) =
        circuit.molecules[id].run {
            when(this) {
                null -> null
                is T -> this
                else -> throw IllegalArgumentException("$this already exist and it is not of type ${T::class}")
            }
        }

    /**
     * Puts a molecule into the circuit.
     *
     * @param molecule the molecule to be put.
     */
    internal fun put(molecule: Molecule) =
        when (molecule) {
            is Gene -> circuit.addGene(molecule)
            is Protein -> circuit.addProtein(molecule)
            is Regulator<*> -> circuit.addRegulator(molecule)
            is RegulativeMolecule -> Unit
            else -> throw IllegalStateException("Unknown molecule class.")
        }

    infix fun then(export: export) = export.wrapper(this)
}