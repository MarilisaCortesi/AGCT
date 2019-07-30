package dsl.model

import model.circuit.BasicGeneticCircuit
import model.circuit.GeneticCircuit
import model.entities.DegradingMolecule
import model.reactions.BasicDegradation

abstract class DslCircuit internal constructor() {
    internal abstract val geneticCircuit : GeneticCircuit
    protected var entities = mutableMapOf<String, DslEntity>()
    protected var reactions = mutableSetOf<DslReaction>()

    internal inline fun<reified E : DslEntity> getOrPutEntity(id: String, ifAbsent: String.() -> E) =
        entities.getOrPut(id) { id.ifAbsent() }.apply {
            if (this !is E) throw IllegalArgumentException("$this already exist but it is not of type ${E::class}")
        } as E

    internal inline fun<reified R : DslReaction> putReaction(reaction: R) =
        reactions.add(reaction)
}

class BasicDslCircuit internal constructor(private val name: String) : DslCircuit() {
    override val geneticCircuit : BasicGeneticCircuit
        get() = BasicGeneticCircuit(name).also { circuit ->
            circuit.addEntities(*entitiesArray)
            circuit.addReactions(*degradationsArray)
            circuit.addReactions(*reactionsArray)
        }

    private val entitiesArray
        get() = entities.values
            .map { it.biochemicalEntity }
            .toSet()
            .toTypedArray()

    private val degradationsArray
        get() = entities.values
            .filterIsInstance<DslDegradingRegulating>()
            .filter { it.degradationRate.rate != null }
            .map { BasicDegradation(it.biochemicalEntity as DegradingMolecule, it.degradationRate.rate!!) }
            .toSet()
            .toTypedArray()

    private val reactionsArray
        get() = reactions
            .map { it.biochemicalReaction }
            .toSet()
            .toTypedArray()
}