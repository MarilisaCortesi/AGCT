package model.circuit

import model.entities.GeneticEntity
import model.reactions.GeneticReaction

interface GeneticCircuit {
    /**
     * The [name] of the circuit.
     */
    val name: String

    /**
     * The [reactions] of the circuit.
     */
    val reactions: Set<GeneticReaction>

    /**
     * The [entities] of the circuit.
     */
    val entities: Set<GeneticEntity>

    /**
     * The [rules] of the circuit.
     */
    val rules: CircuitRules

    /**
     * Checks if all the [rules] are satisfied.
     */
    fun checkRules()

    /**
     * Returns the reactions involving the given [entity].
     */
    fun reactionsOf(entity: GeneticEntity) : Set<GeneticReaction>
}

/**
 * Represents a mutable [genetic circuit][GeneticCircuit].
 */
internal interface MutableGeneticCircuit : GeneticCircuit {
    /**
     * Adds an [entity] into the geneticCircuit.
     */
    fun addEntity(entity: GeneticEntity)

    /**
     * Adds a series of [entities] into the geneticCircuit.
     */
    fun addEntities(vararg entities: GeneticEntity)

    /**
     * Adds a [reaction] into the geneticCircuit.
     */
    fun addReaction(reaction: GeneticReaction)

    /**
     * Adds a series of [reactions] into the geneticCircuit.
     */
    fun addReactions(vararg reactions: GeneticReaction)
}