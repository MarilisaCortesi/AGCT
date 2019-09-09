package model.circuit

import model.entities.GeneticEntity
import model.reactions.GeneticReaction

internal abstract class AbstractGeneticCircuit constructor(override val rules: CircuitRules) : GeneticCircuit {
    constructor(setRules: CircuitRules.() -> Unit) : this(BasicCircuitRules().apply(setRules))

    private val circuitMap = mutableMapOf<GeneticEntity, MutableSet<GeneticReaction>>()

    override val reactions: Set<GeneticReaction>
        get() = circuitMap.values.flatten().toSet()

    override val entities: Set<GeneticEntity>
        get() = circuitMap.keys.toSet()

    override fun checkRules() {
        for (check in rules.exportRules.values) {
            check(circuitMap)
        }
    }

    override fun reactionsOf(entity: GeneticEntity) =
        circuitMap[entity]?.toSet() ?: emptySet()

    override fun addEntity(entity: GeneticEntity) =
        addEntities(entity)

    override fun addEntities(vararg entities: GeneticEntity) {
        for (entity in entities) {
            circuitMap.getOrPut(entity) { mutableSetOf() }
        }
    }

    override fun addReaction(reaction: GeneticReaction) =
        addReactions(reaction)

    override fun addReactions(vararg reactions: GeneticReaction) {
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
