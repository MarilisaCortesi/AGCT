package model.circuit

import model.entities.*
import model.reactions.BiochemicalReaction
import model.reactions.Degradation
import model.reactions.Transcription
import java.lang.IllegalStateException

/**
 * A genetic circuit with basic limitations:
 * - a molecule that can deteriorate, must have one and one deterioration reaction only;
 * - a gene can code for an arbitrary number of proteins, but a protein can be coded by one gene only;
 * - a regulator can regulate an arbitrary number of transcription reactions;
 * - a transcription reaction can have an arbitrary number of regulators;
 *
 * For any of them that is broken, an exception will be thrown.
 */
internal class BasicGeneticCircuit(name: String) : AbstractGeneticCircuit(name) {
    override fun checkOnAdd(entity: BiochemicalEntity, reaction: BiochemicalReaction) {
        circuit.getOrPut(entity) { mutableSetOf() }.run {
            when {
                contains(reaction) ->
                    throw IllegalArgumentException("$reaction already set for $entity.")
                entity is Protein && reaction is Transcription && filterIsInstance<Transcription>().isNotEmpty() ->
                    throw IllegalArgumentException("Transcription reaction already set for $entity.")
                else ->
                    add(reaction)
            }
        }
    }

    override fun checkOnExport() {
        circuit.filterKeys { it is DegradingMolecule }
            .filterValues { it is Degradation }
            .filterValues { it.isNotEmpty() }
            .keys
            .random()
            .apply {
                throw IllegalStateException("Degradation reaction not set for $this")
            }
    }
}