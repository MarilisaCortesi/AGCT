package dsl.model

import model.variables.Concentration
import model.variables.PositiveVariable
import model.variables.Rate

abstract class DslPositiveVariable<T : Number> internal constructor(
    private val construct: Sequence<Number>.() -> PositiveVariable<T>
) {
    internal var value: PositiveVariable<T>? = null

    infix fun of(value: Number) =
        into(sequenceOf(value))

    infix fun into(values: Sequence<Number>) {
        value = values.construct()
    }
}

class DslConcentration internal constructor() : DslPositiveVariable<Double>({ Concentration(this) }) {
    internal val concentration
        get() = (value ?: Concentration()) as Concentration
}

class DslRate internal constructor() : DslPositiveVariable<Double>({ Rate(this) }) {
    internal val rate
        get() = (value ?: Rate()) as Rate
}