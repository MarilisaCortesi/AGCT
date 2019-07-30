package dsl.levels

import dsl.model.*

class CircuitLevel internal constructor(private val circuit: DslCircuit) {
    val the = this

    infix fun gene(id: String) =
        circuit.getOrPutEntity(id) { DslGene(this) }.run { GeneWrapper(this) }

    infix fun protein(id: String) =
        circuit.getOrPutEntity(id) { DslProtein(this) }.run { ProteinWrapper(this) }

    infix fun regulator(id: String) =
        circuit.getOrPutEntity(id) { DslRegulator(this) }.run { RegulatorWrapper(this) }

    fun gene(id: String, block: GeneLevel.() -> Unit) =
        gene(id).that(block)

    fun protein(id: String, block: ProteinLevel.() -> Unit) =
        protein(id).that(block)

    fun regulator(id: String, block: RegulatorLevel.() -> Unit) =
        regulator(id).that(block)
}

abstract class EntityWrapper<out L: EntityLevel<DslEntity>> internal constructor(private val innerLevel: L) {
    infix fun that(block: L.() -> Unit) =
        innerLevel.run(block)

    fun invoke(block: L.() -> Unit) =
        that(block)
}

class GeneWrapper internal constructor(gene: DslGene) :
    EntityWrapper<GeneLevel>(GeneLevel(gene))

class ProteinWrapper internal constructor(protein: DslProtein) :
    EntityWrapper<ProteinLevel>(ProteinLevel(protein))

class RegulatorWrapper internal constructor(regulator: DslRegulator) :
    EntityWrapper<RegulatorLevel>(RegulatorLevel(regulator))