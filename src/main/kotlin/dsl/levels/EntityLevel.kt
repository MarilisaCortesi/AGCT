@file:Suppress("PackageDirectoryMismatch", "ClassName", "UNUSED_PARAMETER", "PropertyName", "FunctionName")

package dsl

import model.utils.type
import java.lang.IllegalStateException

class EntityLevelWrapper<out E: EntityLevel<*, *>> internal constructor(
    private val entityLevel: E
) {
    infix fun that(block: E.() -> Unit) =
        entityLevel.run(block)
}

open class EntityLevel<out E : DslEntity, out S : EntityLevel<E, S>> internal constructor(
    protected val circuit: DslCircuit,
    protected val entity: E
) {
    @Suppress("UNCHECKED_CAST")
    val has
        get() = this as S

    infix fun an(dummy: initial_concentration) = entity.initialConcentration
}

open class DegradingEntityLevel<out E : DslDegradable, out S : DegradingEntityLevel<E, S>> internal constructor(
    circuit: DslCircuit,
    entity: E
) : EntityLevel<E, DegradingEntityLevel<E, S>>(circuit, entity) {
    infix fun a(dummy: degradation_rate) = DslRate().apply { entity.degradationRate = this }
}

class GeneLevel internal constructor(circuit: DslCircuit, gene: DslGene) :
    EntityLevel<DslGene, GeneLevel>(circuit, gene) {
    val codes_for
        get() = CodesFor(entity)

    class CodesFor internal constructor(gene: DslGene) {
        operator fun invoke(block: TranscriptionLevel.() -> Unit) = Unit
    }
}

class ProteinLevel internal constructor(circuit: DslCircuit, protein: DslProtein) :
    DegradingEntityLevel<DslProtein, ProteinLevel>(circuit, protein)

class RegulatorLevel internal constructor(circuit: DslCircuit, molecule: DslRegulating) :
    DegradingEntityLevel<DslRegulating, RegulatorLevel>(circuit, molecule)

class GenericEntityLevel internal constructor(circuit: DslCircuit, entity: DslEntity) :
    EntityLevel<DslEntity, GenericEntityLevel>(circuit, entity) {
    infix fun a(dummy: degradation_rate) =
            if (entity is DslDegradable)
                DslRate().apply { entity.degradationRate = this }
            else
                throw IllegalStateException("\"${entity.id}\" is a ${entity.type.decapitalize()} so it cannot degrade.")
}

operator fun String.invoke(block: GenericEntityLevel.() -> Unit) =
    TopLevel.getEntity(this).let {
        EntityLevelWrapper(GenericEntityLevel(TopLevel.circuit, it)).that(block)
    }

