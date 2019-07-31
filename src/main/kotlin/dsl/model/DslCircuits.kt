@file:Suppress("RemoveExplicitTypeArguments")

package dsl.model

import dsl.utils.ExportObject
import model.circuit.BasicGeneticCircuit
import model.utils.string

abstract class DslCircuit internal constructor() {
    protected var entities = mutableMapOf<String, DslEntity>()
    protected var reactions = mutableSetOf<DslReaction>()

    internal inline fun<reified E : DslEntity> getOrPutEntity(id: String, ifAbsent: String.() -> E) =
        entities.getOrPut(id) { id.ifAbsent() }.apply {
            if (this !is E) throw IllegalArgumentException("$this already exist but it is not of type ${E::class}")
        } as E

    internal fun getOrThrow(id: String) =
        getOrPutEntity<DslEntity>(id) {
            throw java.lang.IllegalArgumentException("Entity ${id.string} has not been set before.")
        }

    internal inline fun<reified R : DslReaction> putReaction(reaction: R) =
        reactions.add(reaction)

    internal abstract fun exportTo(types: Collection<ExportObject>)
}

class BasicDslCircuit internal constructor(private val name: String) : DslCircuit() {
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
            .mapNotNull { it.degradation }
            .map { it.biochemicalReaction }
            .toSet()
            .toTypedArray()

    private val reactionsArray
        get() = reactions
            .map { it.biochemicalReaction }
            .toSet()
            .toTypedArray()
}