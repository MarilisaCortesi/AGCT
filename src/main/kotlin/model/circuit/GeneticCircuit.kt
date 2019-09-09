package model.circuit

import model.entities.GeneticEntity
import model.reactions.GeneticReaction

internal class CircuitRules {
    /**
     * Contain the list of rules to be checked when adding a new reaction to the circuit.
     * A circuit must throw an [IllegalArgumentException] if one of them is not followed.
     */
    val addingRules = mutableMapOf<String, (MutableSet<GeneticReaction>, GeneticEntity, GeneticReaction) -> Unit>()

    /**
     * Contain the list of rules to be followed when exporting the circuit.
     * A circuit must throw an [IllegalStateException] if one of them are not followed.
     */
    val exportRules = mutableMapOf<String, (Map<GeneticEntity, Set<GeneticReaction>>) -> Unit>()
}

abstract class GeneticCircuit internal constructor(private val rules: CircuitRules) {
    internal constructor(setRules: CircuitRules.() -> Unit) : this(CircuitRules().apply(setRules))

    abstract val name: String

    private val circuitMap = mutableMapOf<GeneticEntity, MutableSet<GeneticReaction>>()

    internal val reactions: Set<GeneticReaction>
        get() = circuitMap.values.flatten().toSet()

    internal val entities: Set<GeneticEntity>
        get() = circuitMap.keys.toSet()

    internal fun checkOnExport() {
        for (check in rules.exportRules.values) {
            check(circuitMap)
        }
    }

    /**
     * Returns the reactions involving the given [entity].
     */
    internal fun reactionsOf(entity: GeneticEntity) =
        circuitMap[entity]?.toSet() ?: emptySet()

    /**
     * Adds an [entity] into the geneticCircuit.
     */
    internal fun addEntity(entity: GeneticEntity) =
        addEntities(entity)

    /**
     * Adds a series of [entities] into the geneticCircuit.
     */
    internal fun addEntities(vararg entities: GeneticEntity) {
        for (entity in entities) {
            circuitMap.getOrPut(entity) { mutableSetOf() }
        }
    }

    /**
     * Adds a [reaction] into the geneticCircuit.
     */
    internal fun addReaction(reaction: GeneticReaction) =
        addReactions(reaction)

    /**
     * Adds a series of [reactions] into the geneticCircuit.
     */
    internal fun addReactions(vararg reactions: GeneticReaction) {
        for (reaction in reactions) {
            for (entity in reaction.entities) {
                circuitMap.getOrPut(entity) { mutableSetOf() }.also { set ->
                    for (check in rules.addingRules.values) {
                        check(set, entity, reaction)
                    }
                }.add(reaction)
            }
        }
    }

    private val GeneticReaction.entities
        get() = reactions.flatMap { it.reagents.keys.toMutableSet().apply { addAll(it.products.keys) } }.toSet()
}