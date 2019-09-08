package model.circuit

import model.entities.BiochemicalEntity
import model.reactions.BiochemicalReaction

internal class CircuitRules {
    /**
     * Contain the list of rules to be checked when adding a new reaction to the circuit.
     * A circuit must throw an [IllegalArgumentException] if one of them is not followed.
     */
    val addingRules = mutableMapOf<String, (MutableSet<BiochemicalReaction>, BiochemicalEntity, BiochemicalReaction) -> Unit>()

    /**
     * Contain the list of rules to be followed when exporting the circuit.
     * A circuit must throw an [IllegalStateException] if one of them are not followed.
     */
    val exportRules = mutableMapOf<String, (Map<BiochemicalEntity, Set<BiochemicalReaction>>) -> Unit>()
}

abstract class GeneticCircuit internal constructor(private val rules: CircuitRules) {
    internal constructor(setRules: CircuitRules.() -> Unit) : this(CircuitRules().apply(setRules))

    abstract val name: String

    private val circuitMap = mutableMapOf<BiochemicalEntity, MutableSet<BiochemicalReaction>>()

    internal val reactions: Set<BiochemicalReaction>
        get() = circuitMap.values.flatten().toSet()

    internal val entities: Set<BiochemicalEntity>
        get() = circuitMap.keys.toSet()

    internal fun checkOnExport() {
        for (check in rules.exportRules.values) {
            check(circuitMap)
        }
    }

    /**
     * Returns the reactions involving the given [entity].
     */
    internal fun reactionsOf(entity: BiochemicalEntity) =
        circuitMap[entity]?.toSet() ?: emptySet()

    /**
     * Adds an [entity] into the geneticCircuit.
     */
    internal fun addEntity(entity: BiochemicalEntity) =
        addEntities(entity)

    /**
     * Adds a series of [entities] into the geneticCircuit.
     */
    internal fun addEntities(vararg entities: BiochemicalEntity) {
        for (entity in entities) {
            circuitMap.getOrPut(entity) { mutableSetOf() }
        }
    }

    /**
     * Adds a [reaction] into the geneticCircuit.
     */
    internal fun addReaction(reaction: BiochemicalReaction) =
        addReactions(reaction)

    /**
     * Adds a series of [reactions] into the geneticCircuit.
     */
    internal fun addReactions(vararg reactions: BiochemicalReaction) {
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

    private val BiochemicalReaction.entities
        get() = reactions.flatMap { it.reagents.keys.toMutableSet().apply { addAll(it.products.keys) } }.toSet()
}