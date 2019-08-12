@file:Suppress("PackageDirectoryMismatch", "ClassName", "UNUSED_PARAMETER", "PropertyName", "FunctionName")

package dsl

import model.utils.string
import model.utils.type
import java.lang.IllegalStateException

class EntityLevelWrapper<out E: EntityLevel<*>> internal constructor(
    private val entityLevel: E
) {
    infix fun that(block: E.() -> Unit) =
        entityLevel.run(block)
}

abstract class EntityLevel<out E : DslEntity> internal constructor(
    protected val circuit: DslCircuit,
    id: String,
    ifAbsent: String.() -> E
) {
    @Suppress("UNCHECKED_CAST")
    internal val entity = circuit.getOrPutEntity(id, ifAbsent) as E

    open val has
        get() = Has()

    open inner class Has() {
        infix fun an(dummy: initial_concentration) = entity.initialConcentration
    }

}

abstract class DegradingEntityLevel<out E : DslDegradable> internal constructor(
    circuit: DslCircuit,
    id: String,
    ifAbsent: String.() -> E
) : EntityLevel<E>(circuit, id, ifAbsent) {
    override val has: Has
        get() = Has()

    inner class Has() : EntityLevel<E>.Has() {
        infix fun a(dummy: degradation_rate) = DslRate().apply { entity.degradationRate = this }
    }
}

class GeneLevel internal constructor(circuit: DslCircuit, id: String) :
    EntityLevel<DslGene>(circuit, id, { DslGene(id) }) {
    val codes_for
        get() = CodesFor()

    inner class CodesFor internal constructor() {
        operator fun invoke(block: TranscriptionLevel.() -> Unit) =
            TranscriptionLevel(circuit, entity).block()
    }
}

class ProteinLevel internal constructor(circuit: DslCircuit, id: String) :
    DegradingEntityLevel<DslProtein>(circuit, id, { DslProtein(id) })

class RegulatorLevel internal constructor(circuit: DslCircuit, id: String) :
    DegradingEntityLevel<DslRegulating>(circuit, id, { DslMolecule(id) })

class GenericEntityLevel internal constructor(circuit: DslCircuit, id: String) :
    EntityLevel<DslEntity>(circuit, id, {
        throw IllegalArgumentException("Entity ${id.string} has not been set before.")
    }) {
    override val has: Has
        get() = Has()

    inner class Has() : EntityLevel<*>.Has() {
        infix fun a(dummy: degradation_rate) =
            if (entity is DslDegradable)
                DslRate().apply { entity.degradationRate = this }
            else
                throw IllegalStateException("\"${entity.id}\" is a ${entity.type.decapitalize()} so it cannot degrade.")
    }
}

operator fun String.invoke(block: GenericEntityLevel.() -> Unit) =
    EntityLevelWrapper(GenericEntityLevel(TopLevel.circuit, this)).that(block)

