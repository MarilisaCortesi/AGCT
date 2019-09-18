package model.circuit

import model.entities.Entity
import model.reactions.Reaction

interface GeneticCircuit {
    /**
     * The [name] of the circuit.
     */
    val name: String

    /**
     * The [reactions] of the circuit.
     */
    val reactions: Set<Reaction>

    /**
     * The [entities] of the circuit.
     */
    val entities: Set<Entity>

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
    fun reactionsOf(entity: Entity) : Set<Reaction>
}

/**
 * Represents a mutable [genetic circuit][GeneticCircuit].
 */
internal interface MutableGeneticCircuit : GeneticCircuit {
    /**
     * Adds an [entity] into the circuit.
     */
    fun addEntity(entity: Entity)

    /**
     * Adds a series of [entities] into the circuit.
     */
    fun addEntities(vararg entities: Entity)

    /**
     * Adds a [reaction] into the circuit.
     */
    fun addReaction(reaction: Reaction)

    /**
     * Adds a series of [reactions] into the circuit.
     */
    fun addReactions(vararg reactions: Reaction)
}