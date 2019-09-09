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

interface CircuitRules {
    operator fun String.invoke(addingRule: (MutableSet<GeneticReaction>, GeneticEntity, GeneticReaction) -> Unit)

    operator fun String.invoke(exportRule: (Map<GeneticEntity, Set<GeneticReaction>>) -> Unit)

        /**
     * Contains the list of rules to be checked when adding a new reaction to the circuit.
     * A circuit must throw an [IllegalArgumentException] if one of them is not followed.
     */
    val addingRules: MutableMap<String, (MutableSet<GeneticReaction>, GeneticEntity, GeneticReaction) -> Unit>

    /**
     * Contains the list of rules to be followed when exporting the circuit.
     * A circuit must throw an [IllegalStateException] if one of them are not followed.
     */
    val exportRules: MutableMap<String, (Map<GeneticEntity, Set<GeneticReaction>>) -> Unit>
}