@file:Suppress("PackageDirectoryMismatch", "ClassName", "UNUSED_PARAMETER")

package dsl

import model.utils.type
import java.lang.IllegalStateException

open class EntityLevel<out E : DslEntity> internal constructor(protected val entity: E) {
    open val has: EntityLevel<E>
        get() = this

    infix fun an(dummy: initialConcentration) = entity.initialConcentration
}

open class DegradingEntityLevel<out E : DslDegradable> internal constructor(entity: E): EntityLevel<E>(entity) {
    override val has: DegradingEntityLevel<E>
        get() = this

    infix fun a(dummy: degradationRate) =
        DslRate().apply { entity.degradationRate = this }
}

class GenericEntityLevel internal constructor(entity: DslEntity) : EntityLevel<DslEntity>(entity) {
    override val has: GenericEntityLevel
        get() = this

    infix fun a(dummy: degradationRate) =
        if (entity is DslDegradable) DegradingEntityLevel(entity).a(dummy) else throw cannot("degrade")

    private fun cannot(what: String) =
        IllegalStateException("${entity.id} is a ${entity.type.decapitalize()} so it cannot $what")
}

class GeneLevel internal constructor(gene: DslGene) : EntityLevel<DslGene>(gene)

class ProteinLevel internal constructor(protein: DslProtein) : DegradingEntityLevel<DslProtein>(protein)

class RegulatorLevel internal constructor(molecule: DslRegulating) : DegradingEntityLevel<DslRegulating>(molecule)

