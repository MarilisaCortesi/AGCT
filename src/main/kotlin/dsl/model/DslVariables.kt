package dsl.model

import model.variables.Concentration
import model.variables.PositiveVariable
import model.variables.Rate

abstract class DslPositiveVariable<T : Number> internal constructor(
    private val construct: Sequence<Number>.() -> PositiveVariable<T>
) {
    internal var variable: PositiveVariable<T>? = null

    infix fun of(value: Number) =
        into(sequenceOf(value))

    infix fun into(values: Sequence<Number>) {
        variable = values.construct()
    }
}

class DslConcentration internal constructor() : DslPositiveVariable<Double>({ Concentration(this) }) {
    internal val concentration
        get() = variable as Concentration?
}

class DslRate internal constructor() : DslPositiveVariable<Double>({ Rate(this) }) {
    internal val rate
        get() = variable as Rate?
}