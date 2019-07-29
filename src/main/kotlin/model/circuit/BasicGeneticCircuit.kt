package model.circuit

import model.entities.*
import model.reactions.BiochemicalReaction
import model.reactions.Degradation
import model.reactions.Transcription
import java.lang.IllegalStateException

/**
 * A genetic geneticCircuit with basic limitations:
 * - a molecule that can deteriorate, must have one and one deterioration biochemicalReaction only;
 * - a gene can code for an arbitrary number of proteins, but a protein can be coded by one gene only;
 * - a regulator can regulate an arbitrary number of transcription reactions;
 * - a transcription biochemicalReaction can have an arbitrary number of regulators;
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
                    throw IllegalArgumentException("Transcription biochemicalReaction already set for $entity.")
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
            .takeIf { it.isNotEmpty() }
            .run {
                throw IllegalStateException("Degradation biochemicalReaction not set for $this")
            }
    }
}