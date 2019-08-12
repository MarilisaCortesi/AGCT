@file:Suppress("PackageDirectoryMismatch", "UNUSED_PARAMETER")

package dsl

val Create = TopLevel()

class TopLevel internal constructor() {
    companion object {
        private var companionCircuit: DslCircuit? = null

        internal val circuit
            get() = companionCircuit ?: throw IllegalStateException("No circuit is running.")

        internal fun getEntity(id: String) =
            circuit.getOrThrow(id)
    }

    infix fun circuit(name: String) =
        CircuitWrapper(name)

    class CircuitWrapper internal constructor(private val name: String) {
        private val defaultRoutines = mutableListOf<MutableDefaultValues.() -> Unit>()
        private val circuitRoutines = mutableListOf<ContainingLevel.() -> Unit>()

        infix fun with(block: MutableDefaultValues.() -> Unit) =
            defaultRoutines.add(block).let { this }

        infix fun containing(block: ContainingLevel.() -> Unit) =
            circuitRoutines.add(block).let { this }

        infix fun then(dummy: export) =
            MutableDefaultValues().run {
                for (routine in defaultRoutines) {
                    routine()
                }
                BasicDslCircuit(name, immutable)
            }.let { circuit ->
                companionCircuit = circuit
                ContainingLevel(circuit).run {
                    for (routine in circuitRoutines) {
                        routine()
                    }
                }
                companionCircuit = null
                CircuitExportFirst(circuit)
            }
    }
}

class ContainingLevel internal constructor(
    private val circuit: DslCircuit
) {
    val the
        get() = this

    infix fun gene(id: String) =
        circuit.getOrPutEntity(id) { DslGene(this) }.let { gene ->
            EntityLevelWrapper(GeneLevel(circuit, gene))
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