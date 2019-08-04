@file:Suppress("PackageDirectoryMismatch", "UNUSED_PARAMETER")

package dsl

import model.utils.create

data class ValuesContainer internal constructor(
    val initialConcentration: DslConcentration = DslConcentration(),
    val degradationRate: DslRate = DslRate(),
    val basalRate: DslRate = DslRate(),
    val regulatedRate: DslRate = DslRate(),
    val bindingRate: DslRate = DslRate(),
    val unbindingRate: DslRate = DslRate()
)

open class DefaultValues internal constructor(protected val container: ValuesContainer) {
    internal val initialConcentration
        get() = container.initialConcentration.get

    internal val degradationRate
        get() = container.degradationRate.get

    internal val basalRate
        get() = container.basalRate.get

    internal val regulatedRate
        get() = container.regulatedRate.get

    internal val bindingRate
        get() = container.bindingRate.get

    internal val unbindingRate
        get() = container.unbindingRate.get

    protected open val <T : DslVariable> T.get
        get() = javaClass.kotlin.create(value)
}

class MutableDefaultValues internal constructor() : DefaultValues(ValuesContainer()) {
    val a = this

    internal val immutable
        get() = DefaultValues(container)

    override val <T : DslVariable> T.get
        get() = this

    infix fun default(dummy: initialConcentration) = container.initialConcentration
    infix fun default(dummy: degradationRate) = container.degradationRate
    infix fun default(dummy: basalRate) = container.basalRate
    infix fun default(dummy: regulatedRate) = container.regulatedRate
    infix fun default(dummy: bindingRate) = container.bindingRate
    infix fun default(dummy: unbindingRate) = container.unbindingRate
}

//open class DefaultValues internal constructor(
//    protected val dslInitialConcentration: DslConcentration = DslConcentration(),
//    protected val dslDegradationRate: DslRate = DslRate(),
//    protected val dslBasalRate: DslRate = DslRate(),
//    protected val dslRegulatedRate: DslRate = DslRate(),
//    protected val dslBindingRate: DslRate = DslRate(),
//    protected val dslUnbindingRate: DslRate = DslRate()
//) {
//    internal val initialConcentration: Concentration
//        get() = dslInitialConcentration.concentration
//
//    internal val degradationRate: Rate
//        get() = dslDegradationRate.rate
//
//    internal val basalRate: Rate
//        get() = dslBasalRate.rate
//
//    internal val regulatedRate: Rate
//        get() = dslRegulatedRate.rate
//
//    internal val bindingRate: Rate
//        get() = dslBindingRate.rate
//
//    internal val unbindingRate: Rate
//        get() = dslUnbindingRate.rate
//}
//
//class MutableDefaultValues internal constructor() : DefaultValues() {
//    val a = this
//
//    internal val immutable
//        get() = DefaultValues(
//            DslConcentration(initialConcentration),
//            DslRate(degradationRate),
//            DslRate(basalRate),
//            DslRate(regulatedRate),
//            DslRate(bindingRate),
//            DslRate(unbindingRate)
//        )
//
//    infix fun default(dummy: initialConcentration) = dslInitialConcentration
//    infix fun default(dummy: degradationRate) = dslDegradationRate
//    infix fun default(dummy: basalRate) = dslBasalRate
//    infix fun default(dummy: regulatedRate) = dslRegulatedRate
//    infix fun default(dummy: bindingRate) = dslBindingRate
//    infix fun default(dummy: unbindingRate) = dslUnbindingRate
//}