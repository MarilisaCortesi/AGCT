package model.circuit

import model.entities.*
import model.reactions.BiochemicalReaction

/**
 * Represents a [genetic circuit][GeneticCircuit] with a [name], its [entities] and the [reactions] between them.
 */
internal interface GeneticCircuit {
    val name: String
    val entities: Set<BiochemicalEntity>
    val reactions: Set<BiochemicalReaction>

    /**
     * Adds a series of [reactions] to the circuit.
     */
    fun add(vararg reactions: BiochemicalReaction): GeneticCircuit
}