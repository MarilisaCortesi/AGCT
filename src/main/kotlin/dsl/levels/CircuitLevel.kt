@file:Suppress("PackageDirectoryMismatch")

package dsl

class CircuitLevel internal constructor(private val circuit: DslCircuit) {
    val the = this

    infix fun gene(id: String) =
        LevelWrapper(GeneLevel(circuit.getOrPutEntity(id) { DslGene(circuit.default,this) }))

//    infix fun protein(id: String) =
//        LevelWrapper(ProteinLevel(circuit.getOrPutEntity(id) { DslProtein(circuit.default,this) }))
//
//    infix fun molecule(id: String) =
//        LevelWrapper(RegulatorLevel(circuit.getOrPutEntity(id) { DslMolecule(circuit.default,this) }))
}

class LevelWrapper<out L: EntityLevel<*>> internal constructor(private val innerLevel: L) {
    infix fun that(block: L.() -> Unit) =
        innerLevel.run(block)
}

operator fun String.invoke(block: GenericEntityLevel.() -> Unit) =
    TopLevel.getEntity(this).let {
        LevelWrapper(EntityLevel(it)).that { GenericEntityLevel(it).block() }
    }