@file:Suppress("PackageDirectoryMismatch", "RemoveExplicitTypeArguments", "UNUSED_PARAMETER")

package dsl

import model.circuit.BasicGeneticCircuit
import model.entities.DegradingEntity
import model.reactions.BasicDegradation
import model.utils.string

abstract class DslCircuit internal constructor() {
    protected val entities = mutableMapOf<String, DslEntity>()

    protected val reactions = mutableSetOf<DslReaction>()

    internal abstract val default: ImmutableDefaultValues

    internal inline fun<reified E : DslEntity> getOrPutEntity(id: String, ifAbsent: String.() -> E) =
        entities.getOrPut(id) { id.ifAbsent() }.apply {
            if (this !is E) {
                throw IllegalArgumentException("$this already exist but it is not of type ${E::class}")
            }
        } as E

    internal fun getOrThrow(id: String) =
        entities.getOrElse(id) {
            throw IllegalArgumentException("Entity ${id.string} has not been set before.")
        }

    internal fun putReaction(reaction: DslReaction) {
        reactions.add(reaction)
    }

    internal abstract fun exportTo(types: Collection<ExportObject>)
}

class BasicDslCircuit internal constructor(
    private val name: String,
    override val default: ImmutableDefaultValues
) : DslCircuit() {
    override fun exportTo(types: Collection<ExportObject>) =
        BasicGeneticCircuit(name).also { circuit ->
            circuit.addEntities(*entitiesArray)
            circuit.addReactions(*degradationsArray, *reactionsArray)
        }.exportTo(*types.map { it.type }.toTypedArray())

    private val entitiesArray
        get() = entities.values
            .map { it.biochemicalEntity }
            .toSet()
            .toTypedArray()

    private val degradationsArray
        get() = entities.values
            .filterIsInstance<DslDegradable>()
            .mapNotNull { it.degradationRate?.to(it) }
            .map { (rate, entity) ->
                BasicDegradation(entity.biochemicalEntity as DegradingEntity, rate.value)
            }
            .toSet()
            .toTypedArray()

    private val reactionsArray
        get() = reactions
            .map { it.biochemicalReaction }
            .toSet()
            .toTypedArray()
}