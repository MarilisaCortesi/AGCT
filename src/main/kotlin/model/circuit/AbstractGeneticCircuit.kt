package model.circuit

import model.entities.BiochemicalEntity
import model.reactions.BiochemicalReaction
import model.reactions.Reaction

internal abstract class AbstractGeneticCircuit(override val name: String) : GeneticCircuit {
    protected val circuit = mutableMapOf<BiochemicalEntity, MutableSet<BiochemicalReaction>>()

    override val entities: Set<BiochemicalEntity>
        get() = circuit.keys.toSet()

    override val reactions: Set<BiochemicalReaction>
        get() = circuit.values.flatten().toSet()

    /**
     * Returns a set of entities of type [E].
     */
    inline fun<reified E : BiochemicalEntity> entitiesOf() =
        entities.filterIsInstance<E>().toSet()

    /**
     * Returns a set of reactions of type [R].
     */
    inline fun<reified R : Reaction> reactionsOf() =
        reactions.filterIsInstance<R>().toSet()

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
                checkOnAdd(entity, reaction)
            }
        }
    }

    override fun exportTo(vararg types: ExportTypes) {
        checkOnExport()
        for (type in types) {
            type.from(this)
        }
    }

    /**
     * Implements the rules to be followed when adding a new biochemicalReaction to the geneticCircuit.
     * Adds the [entity] and the [reaction] to the geneticCircuit or throws an [IllegalArgumentException].
     */
    protected abstract fun checkOnAdd(entity: BiochemicalEntity, reaction: BiochemicalReaction)

    /**
     * Implements the rules to be followed when adding a new biochemicalReaction to the geneticCircuit.
     * Throws an [IllegalStateException] if the rules are not covered.
     */
    protected abstract fun checkOnExport()
}

private val BiochemicalReaction.entities
    get() = reactions.flatMap { it.reagents.keys.toMutableSet().apply { addAll(it.products.keys) } }.toSet()