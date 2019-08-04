@file:Suppress("PackageDirectoryMismatch", "ClassName", "UNUSED_PARAMETER")

package dsl

import model.utils.type
import java.lang.IllegalStateException

open class EntityLevel<out E : DslEntity> internal constructor(protected val entity: E) {
    open val has: EntityLevel<E>
        get() = this

    infix fun an(dummy: initialConcentration) = entity.initialConcentration
}

class DegradableEntityLevel<out E : DslDegradable> internal constructor(entity: E): EntityLevel<E>(entity) {
    override val has: DegradableEntityLevel<E>
        get() = this

    infix fun a(dummy: degradationRate) = entity.run {
        degradationRate = DslRate()
        degradationRate!!
    }
}

class GenericEntityLevel internal constructor(entity: DslEntity) : EntityLevel<DslEntity>(entity) {
    override val has: GenericEntityLevel
        get() = this

    infix fun a(dummy: degradationRate) =
        degradableLevel.a(dummy)

    private val degradableLevel
        get() = if (entity is DslDegradable) DegradableEntityLevel(entity) else throw "degrade".exception

    private val String.exception
        get() = IllegalStateException("${entity.id} is a ${entity.type.decapitalize()} so it cannot $this")
}