@file:Suppress("PackageDirectoryMismatch", "UNUSED_PARAMETER")

package dsl

val Fill = TopLevel()

class TopLevel internal constructor() {
    companion object {
        private var companionCircuit: DslCircuit? = null

        internal val circuit
            get() = companionCircuit ?: throw IllegalStateException("No circuit is running.")
    }

    infix fun circuit(name: String) =
        CircuitWrapper(name)

    class CircuitWrapper internal constructor(private val name: String) {
        private val defaultRoutines = mutableListOf<DefaultsLevel.() -> Unit>()
        private val circuitRoutines = mutableListOf<ContainingLevel.() -> Unit>()

        infix fun having(block: DefaultsLevel.() -> Unit) =
            defaultRoutines.add(block).let { this }

        infix fun with(block: ContainingLevel.() -> Unit) =
            circuitRoutines.add(block).let { this }

        infix fun then(dummy: export) =
            MutableDefaultValues().let { defaults ->
                DefaultsLevel(defaults).run {
                    for (routine in defaultRoutines) {
                        routine()
                    }
                }
                BasicDslCircuit(name, defaults.immutable)
            }.let { circuit ->
                companionCircuit = circuit
                ContainingLevel().run {
                    for (routine in circuitRoutines) {
                        routine()
                    }
                }
                companionCircuit = null
                CircuitExport(circuit)
            }
    }
}

class DefaultsLevel internal constructor(private val defaults: MutableDefaultValues) {
    val default
        get() = this

    infix fun initial(dummy: concentration) = defaults.initialConcentration
    infix fun degradation(dummy: rate) = defaults.degradationRate
    infix fun basal(dummy: rate) = defaults.basalRate
    infix fun regulated(dummy: rate) = defaults.regulatedRate
    infix fun binding(dummy: rate) = defaults.bindingRate
    infix fun unbinding(dummy: rate) = defaults.unbindingRate
}

class CircuitExport internal constructor(private val circuit: DslCircuit) {
    infix fun to(type: ExportObject) =
        to(setOf(type))

    infix fun to(types: Collection<ExportObject>) =
        And().and(types)

    inner class And internal constructor() {
        infix fun and(type: ExportObject) =
            and(setOf(type))

        internal fun and(types: Collection<ExportObject>) =
            apply { circuit.exportTo(types) }
    }
}