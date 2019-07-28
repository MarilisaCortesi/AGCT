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

    /**
     * Adds a series of [reactions] into the genetic circuit.
     *
     * @throws IllegalArgumentException if the reaction does not follow the circuit rules.
     */
    override fun add(vararg reactions: BiochemicalReaction): GeneticCircuit {
        for (reaction in reactions) {
            for (entity in reaction.entities) {
                checkOnAdd(entity, reaction)
            }
        }
        return this
    }

    /**
     * Implements the rules to be followed when adding a new reaction to the circuit.
     * Adds the [entity] and the [reaction] to the circuit or throws an [IllegalArgumentException].
     */
    protected abstract fun checkOnAdd(entity: BiochemicalEntity, reaction: BiochemicalReaction)

    /**
     * Implements the rules to be followed when adding a new reaction to the circuit.
     * Throws an [IllegalStateException] if the rules are not covered.
     */
    protected abstract fun checkOnExport()
}

private val BiochemicalReaction.entities
    get() = reactions.flatMap { it.reagents.keys.toMutableSet().apply { addAll(it.products.keys) } }.toSet()