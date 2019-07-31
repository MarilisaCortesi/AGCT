package model.circuit

import model.circuit.export.ExportTypes
import model.entities.BiochemicalEntity
import model.reactions.BiochemicalReaction

internal abstract class AbstractGeneticCircuit(override val name: String) : GeneticCircuit {
    private val circuit = mutableMapOf<BiochemicalEntity, MutableSet<BiochemicalReaction>>()

    override val reactions: Set<BiochemicalReaction>
        get() = circuit.values.flatten().toSet()

    override val entities: Set<BiochemicalEntity>
        get() = circuit.keys.toSet()

    override fun reactionsOf(entity: BiochemicalEntity) =
        circuit[entity]?.toSet() ?: emptySet()

    override fun addEntity(entity: BiochemicalEntity) =
        addEntities(entity)

    override fun addEntities(vararg entities: BiochemicalEntity) {
        for (entity in entities) {
            circuit.getOrPut(entity) { mutableSetOf() }
        }
    }

    override fun addReaction(reaction: BiochemicalReaction) =
        addReactions(reaction)

    override fun addReactions(vararg reactions: BiochemicalReaction) {
        for (reaction in reactions) {
            for (entity in reaction.entities) {
                circuit.getOrPut(entity) { mutableSetOf() }.also { set ->
                    for (check in addingRules.values) {
                        check(set, entity, reaction)
                    }
                }.add(reaction)
            }
        }
    }

    override fun exportTo(vararg types: ExportTypes) {
        for (check in exportRules.values) {
            check(circuit)
        }

        for (type in types) {
            type.from(this)
        }
    }

    /**
     * The list of rules to be checked when adding a new reaction to the circuit.
     * Must throw an [IllegalArgumentException] if the rule is not followed.
     */
    protected val addingRules = mutableMapOf<String, (MutableSet<BiochemicalReaction>, BiochemicalEntity, BiochemicalReaction) -> Unit>()

    /**
     * Implements the rules to be followed when exporting the circuit.
     * Must throw an [IllegalStateException] if the rules are not followed.
     */
    protected val exportRules = mutableMapOf<String, (Map<BiochemicalEntity, Set<BiochemicalReaction>>) -> Unit>()
}

private val BiochemicalReaction.entities
    get() = reactions.flatMap { it.reagents.keys.toMutableSet().apply { addAll(it.products.keys) } }.toSet()