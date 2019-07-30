@file:Suppress("UNUSED_PARAMETER", "ClassName")

package dsl.levels

import dsl.model.*
import dsl.utils.ExportObject

val Create = TopLevel()

object export

class TopLevel internal constructor() {
    infix fun circuit(name: String) = BasicDslCircuit(name).run { CircuitWrapper(this) }
}

class CircuitWrapper internal constructor(private val circuit: DslCircuit) {
    infix fun containing(block: CircuitLevel.() -> Unit) =
        CircuitLevel(circuit).apply(block).run { CircuitWrapper(circuit) }

    infix fun then(dummy: export) =
        CircuitExportFirst(circuit)
}

class CircuitExportFirst internal constructor(private val circuit: DslCircuit) {
    infix fun to(type: ExportObject) =
        to(setOf(type))

    infix fun to(types: Collection<ExportObject>) =
        CircuitExportSecond(circuit).and(types)
}

class CircuitExportSecond internal constructor(private val circuit: DslCircuit) {
    infix fun and(type: ExportObject) =
        and(setOf(type))

    internal fun and(types: Collection<ExportObject>) =
        apply { types.forEach { it.export(circuit.geneticCircuit) } }
}