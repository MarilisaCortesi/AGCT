package dsl.levels

import dsl.model.*

class CircuitLevel internal constructor(private val circuit: DslCircuit) {
    val the = this

    infix fun gene(id: String) =
        LevelWrapper(EntityLevel(circuit.getOrPutEntity(id) { DslGene(this) }))

    infix fun protein(id: String) =
        LevelWrapper(DegradableEntityLevel(circuit.getOrPutEntity(id) { DslProtein(this) }))

    infix fun molecule(id: String) =
        LevelWrapper(DegradableEntityLevel(circuit.getOrPutEntity<DslRegulating>(id) { DslMolecule(this) }))

    /*
    fun gene(id: String, block: GeneLevel.() -> Unit) =
        gene(id).that(block)

    fun protein(id: String, block: ProteinLevel.() -> Unit) =
        protein(id).that(block)

    fun molecule(id: String, block: RegulatorLevel.() -> Unit) =
        molecule(id).that(block)
    */
}

operator fun String.invoke(block: GenericEntityLevel.() -> Unit) =
    TopLevel.circuit.getOrThrow(this).let {
        LevelWrapper(EntityLevel(it)).that { GenericEntityLevel(it).block() }
    }


class LevelWrapper<out L: EntityLevel<*>> internal constructor(private val innerLevel: L) {
    infix fun that(block: L.() -> Unit) =
        innerLevel.run(block)
}