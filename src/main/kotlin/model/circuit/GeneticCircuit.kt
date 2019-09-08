package model.circuit

import model.entities.BiochemicalEntity
import model.reactions.BiochemicalReaction

/**
 * The [addingRules] contain the list of rules to be checked when adding a new reaction to the circuit.
 * The circuit must throw an [IllegalArgumentException] if one of the rules is not followed.
 *
 * The [exportRules] contain the list of rules to be followed when exporting the circuit.
 * The circuit must throw an [IllegalStateException] if the rules are not followed.
*/
internal class CircuitRules {
    val addingRules = mutableMapOf<String, (MutableSet<BiochemicalReaction>, BiochemicalEntity, BiochemicalReaction) -> Unit>()
    val exportRules = mutableMapOf<String, (Map<BiochemicalEntity, Set<BiochemicalReaction>>) -> Unit>()
}

abstract class GeneticCircuit internal constructor(rules: CircuitRules) {
    internal constructor(setRules: CircuitRules.() -> Unit) : this(CircuitRules().apply(setRules))

    abstract val name: String

    private val mutableCircuitMap = mutableMapOf<BiochemicalEntity, MutableSet<BiochemicalReaction>>()

    internal val circuitMap: Map<BiochemicalEntity, Set<BiochemicalReaction>>
        get() = mutableCircuitMap.mapValues { it.value.toSet() }.toMap()

    internal val reactions: Set<BiochemicalReaction>
        get() = circuitMap.values.flatten().toSet()

    internal val entities: Set<BiochemicalEntity>
        get() = circuitMap.keys.toSet()

    internal val addingRules = rules.addingRules.toMap()

    internal val exportRules = rules.exportRules.toMap()

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
            mutableCircuitMap.getOrPut(entity) { mutableSetOf() }
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
                mutableCircuitMap.getOrPut(entity) { mutableSetOf() }.also { set ->
                    for (check in addingRules.values) {
                        check(set, entity, reaction)
                    }
                }.add(reaction)
            }
        }
    }

    private val BiochemicalReaction.entities
        get() = reactions.flatMap { it.reagents.keys.toMutableSet().apply { addAll(it.products.keys) } }.toSet()
}