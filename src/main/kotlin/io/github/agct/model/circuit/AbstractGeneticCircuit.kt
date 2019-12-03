package io.github.agct.model.circuit

import io.github.agct.model.entities.Entity
import io.github.agct.model.reactions.Reaction

internal abstract class AbstractGeneticCircuit constructor(override val rules: CircuitRules) :
    MutableGeneticCircuit {
    constructor(setRules: MutableCircuitRules.() -> Unit) : this(BasicCircuitRules().apply(setRules))

    private val circuitMap = mutableMapOf<Entity, MutableSet<Reaction>>()

    override val reactions: Set<Reaction>
        get() = circuitMap.values.flatten().toSet()

    override val entities: Set<Entity>
        get() = circuitMap.keys.toSet()

    override fun checkRules() {
        for (check in rules.exportRules.values) {
            check(circuitMap)
        }
    }

    override fun reactionsOf(entity: Entity) =
        circuitMap[entity]?.toSet() ?: emptySet()

    override fun addEntity(entity: Entity) =
        addEntities(entity)

    override fun addEntities(vararg entities: Entity) {
        for (entity in entities) {
            circuitMap.getOrPut(entity) { mutableSetOf() }
        }
    }

    override fun addReaction(reaction: Reaction) =
        addReactions(reaction)

    override fun addReactions(vararg reactions: Reaction) {
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

    private val Reaction.entities
        get() = reactions.flatMap { it.reagents.keys.toMutableSet().apply { addAll(it.products.keys) } }.toSet()
}