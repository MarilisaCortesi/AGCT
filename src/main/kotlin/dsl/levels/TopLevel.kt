@file:Suppress("UNUSED_PARAMETER")

package dsl.levels

import dsl.*
import dsl.model.*
import dsl.utils.ExportObject

val Create = TopLevel()

class TopLevel internal constructor() {
    infix fun circuit(name: String) =
        BasicDslCircuit(name).apply {
            if (privateCircuit == null) {
                privateCircuit = this
            } else {
                throw IllegalStateException("Another circuit is already running.")
            }
        }.run { CircuitWrapper() }

    class CircuitWrapper internal constructor() {
        infix fun with(block: DslCircuit.DefaultValues.() -> Unit) =
            default.block().let { this }

        infix fun containing(block: CircuitLevel.() -> Unit) =
            CircuitLevel(circuit).apply(block).let { this }

        infix fun then(dummy: export) =
            CircuitExportFirst(circuit).also { privateCircuit = null }
    }

    companion object Circuit {
        private var privateCircuit: DslCircuit? = null

        internal val circuit
            get() = privateCircuit ?: throw IllegalStateException("No circuits running.")

        internal val default
            get() = circuit.defaultValues
    }
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
        apply { circuit.exportTo(types) }
}