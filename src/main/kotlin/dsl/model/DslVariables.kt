package dsl.model

import model.variables.Concentration
import model.variables.PositiveVariable
import model.variables.Rate

abstract class DslVariable internal constructor(value: PositiveVariable<*>?) {
    internal abstract val Sequence<Number>.construct: PositiveVariable<Number>

    internal abstract val copy: DslVariable

    internal var value: PositiveVariable<Number>? = value

    infix fun of(value: Number) =
        into(sequenceOf(value))

    infix fun into(values: Sequence<Number>) {
        value = values.construct
    }
}

class DslConcentration internal constructor(value: Concentration? = null) : DslVariable(value) {
    override val Sequence<Number>.construct: Concentration
        get() = Concentration(this)

    override val copy: DslConcentration
        get() = DslConcentration(value as Concentration?)

    internal val concentration
        get() = (value ?: Concentration()) as Concentration
}

class DslRate internal constructor(value: Rate? = null) : DslVariable(value) {
    override val Sequence<Number>.construct: Rate
        get() = Rate(this)

    override val copy: DslRate
        get() = DslRate(value as Rate?)

    internal val rate
        get() = (value ?: Rate()) as Rate
}