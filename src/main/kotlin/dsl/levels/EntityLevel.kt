@file:Suppress("ClassName", "UNUSED_PARAMETER")

package dsl.levels

import dsl.model.*

object initialConcentration
object degradationRate

abstract class EntityLevel<out E : DslEntity> internal constructor(protected val entity: E) {
    abstract val has: EntityLevel<E>

    infix fun an(dummy: initialConcentration) = entity.initialConcentration
}

abstract class DegradingEntityLevel<out E : DslDegradingRegulating> internal constructor(entity: E): EntityLevel<E>(entity) {
    infix fun a(dummy: degradationRate) = entity.degradationRate
}

class GeneLevel internal constructor(gene: DslGene) : EntityLevel<DslGene>(gene) {
    override val has: GeneLevel
        get() = this
}

class ProteinLevel internal constructor(protein: DslProtein) : DegradingEntityLevel<DslProtein>(protein) {
    override val has: ProteinLevel
        get() = this
}

class RegulatorLevel internal constructor(regulator: DslRegulator) : DegradingEntityLevel<DslRegulator>(regulator) {
    override val has: RegulatorLevel
        get() = this
}