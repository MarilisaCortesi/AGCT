@file:Suppress("PackageDirectoryMismatch", "UNUSED_PARAMETER")

package dsl

val Create = TopLevel()

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

        infix fun with(block: DefaultsLevel.() -> Unit) =
            defaultRoutines.add(block).let { this }

        infix fun containing(block: ContainingLevel.() -> Unit) =
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
                ContainingLevel(circuit).run {
                    for (routine in circuitRoutines) {
                        routine()
                    }
                }
                companionCircuit = null
                CircuitExport(circuit)
            }
    }
}

class ContainingLevel internal constructor(private val circuit: DslCircuit) {
    val the
        get() = this

    infix fun gene(id: String) =
        EntityLevelWrapper(GeneLevel(id))
}

class DefaultsLevel internal constructor(private val defaults: MutableDefaultValues) {
    val a
        get() = A()

    inner class A internal constructor() {
        infix fun default(dummy: initial.Concentration) = defaults.initialConcentration
        infix fun default(dummy: degradation.Rate) = defaults.degradationRate
        infix fun default(dummy: basal.Rate) = defaults.basalRate
        infix fun default(dummy: regulating.Rate) = defaults.regulatingRate
        infix fun default(dummy: binding.Rate) = defaults.bindingRate
        infix fun default(dummy: unbinding.Rate) = defaults.unbindingRate
    }
}

class CircuitExport internal constructor(private val circuit: DslCircuit) {
    infix fun to(type: ExportObject) =
        And().and(type)

    infix fun to(dummy: each) = Each()

    inner class And internal constructor() {
        infix fun and(type: ExportObject) =
            apply { circuit.exportTo(setOf(type)) }
    }

    inner class Each internal constructor() {
        infix fun one(types: Collection<ExportObject>) =
            circuit.exportTo(types)
    }
}