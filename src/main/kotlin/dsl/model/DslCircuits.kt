@file:Suppress("PackageDirectoryMismatch", "RemoveExplicitTypeArguments", "UNUSED_PARAMETER")

package agct

import model.circuit.AbstractGeneticCircuit
import model.circuit.BasicGeneticCircuit
import model.entities.DegradingEntity
import model.reactions.BasicDegradation

abstract class DslCircuit internal constructor() {
    protected val entities = mutableMapOf<String, DslEntity>()

    protected val reactions = mutableSetOf<DslReaction>()

    internal abstract val geneticCircuit: AbstractGeneticCircuit

    internal abstract val default: ImmutableDefaultValues

    internal fun getOrPutEntity(id: String, ifAbsent: String.() -> DslEntity) =
        entities.getOrPut(id) { id.ifAbsent() }

    internal fun putReaction(reaction: DslReaction) {
        reactions.add(reaction)
    }
}

class BasicDslCircuit internal constructor(
    private val name: String,
    override val default: ImmutableDefaultValues
) : DslCircuit() {
    override val geneticCircuit: AbstractGeneticCircuit
        get() =  BasicGeneticCircuit(name).apply {
            addEntities(*entitiesArray)
            addReactions(*degradationsArray, *reactionsArray)
        }

    private val entitiesArray
        get() = entities.values
            .map { it.modelEntity }
            .toSet()
            .toTypedArray()

    private val degradationsArray
        get() = entities.values
            .filterIsInstance<DslDegradable>()
            .mapNotNull { it.degradationRate?.to(it) }
            .map { (rate, entity) ->
                BasicDegradation(entity.modelEntity as DegradingEntity, rate.value)
            }
            .toSet()
            .toTypedArray()

    private val reactionsArray
        get() = reactions
            .map { it.modelReaction }
            .toSet()
            .toTypedArray()
}