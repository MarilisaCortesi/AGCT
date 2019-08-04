@file:Suppress("PackageDirectoryMismatch", "UNUSED_PARAMETER")

package dsl

val Create = TopLevel()

class TopLevel internal constructor() {
    companion object {
        private var companionCircuit: DslCircuit? = null

        internal fun getEntity(id: String) =
            companionCircuit?.getOrThrow(id) ?: throw IllegalStateException("No circuit is running.")
    }

    infix fun circuit(name: String) =
        CircuitWrapper(name)

    class CircuitWrapper internal constructor(private val name: String) {
        private val defaultRoutines = mutableListOf<MutableDefaultValues.() -> Unit>()
        private val circuitRoutines = mutableListOf<CircuitLevel.() -> Unit>()

        infix fun with(block: MutableDefaultValues.() -> Unit) =
            defaultRoutines.add(block).let { this }

        infix fun containing(block: CircuitLevel.() -> Unit) =
            circuitRoutines.add(block).let { this }

        infix fun then(dummy: export) =
            MutableDefaultValues().run {
                for (routine in defaultRoutines) {
                    routine()
                }
                BasicDslCircuit(name, immutable)
            }.let { circuit ->
                companionCircuit = circuit
                CircuitLevel(circuit).run {
                    for (routine in circuitRoutines) {
                        routine()
                    }
                }
                companionCircuit = null
                CircuitExportFirst(circuit)
            }
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