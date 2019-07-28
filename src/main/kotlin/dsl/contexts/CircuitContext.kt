package dsl.contexts

/*
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
     * Checks if a [molecule][id] exists.
     * If not returns null, otherwise returns it only if it has type [T].
     */
    internal inline fun<reified T : BiochemicalEntity> getAndCheck(id: String) =
        circuit.entities[id].run {
            when(this) {
                null -> null
                is T -> this
                else -> throw IllegalArgumentException("$this already exist and it is not of type ${T::class}")
            }
        }

    /**
     * Puts a [molecule] into the circuit.
     */
    internal fun put(molecule: BiochemicalEntity) =
        when (molecule) {
            is Gene -> circuit.addGene(molecule)
            is Protein -> circuit.addProtein(molecule)
            is RegulativeMolecule -> circuit.addRegulator(molecule)
            else -> throw IllegalStateException("Unknown molecule class.")
        }

    infix fun then(export: export) = export.wrapper(this)
}
*/