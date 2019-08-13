@file:Suppress("PackageDirectoryMismatch", "ClassName", "UNUSED_PARAMETER", "PropertyName", "FunctionName")

package dsl

import dsl.TopLevel.Companion.circuit
import model.utils.string
import model.utils.type
import java.lang.IllegalStateException

abstract class EntityLevel<out E : DslEntity> internal constructor(
    id: String,
    ifAbsent: String.() -> E
) {
    @Suppress("UNCHECKED_CAST")
    internal val entity = circuit.getOrPutEntity(id, ifAbsent) as E

    open val an
        get() = An()

    open inner class An() {
        infix fun initial(dummy: concentration) = entity.initialConcentration
    }

}

abstract class DegradingEntityLevel<out E : DslDegradable> internal constructor(
    id: String,
    ifAbsent: String.() -> E
) : EntityLevel<E>(id, ifAbsent) {
    open val a: A
        get() = A()

    open inner class A() {
        infix fun degradation(dummy: rate) = DslRate().apply { entity.degradationRate = this }
    }
}

class GeneLevel internal constructor(id: String) : EntityLevel<DslGene>(id, { DslGene(id) })

class ProteinLevel internal constructor(id: String) : DegradingEntityLevel<DslProtein>(id, { DslProtein(id) })

class RegulatorLevel internal constructor(id: String) : DegradingEntityLevel<DslRegulating>(id, { DslMolecule(id) })

class GenericEntityLevel internal constructor(id: String) : EntityLevel<DslEntity>(id, {
        throw IllegalArgumentException("Entity ${id.string} has not been set before.")
    }) {
    val a: A
        get() = A()

    inner class A() {
        infix fun degradation(dummy: rate) =
            if (entity is DslDegradable)
                DslRate().apply { entity.degradationRate = this }
            else
                throw IllegalStateException("\"${entity.id}\" is a ${entity.type.decapitalize()} so it cannot degrade.")
    }
}

infix fun String.has(block: GenericEntityLevel.() -> Unit) =
    EntityWrapper(this).having(block)

