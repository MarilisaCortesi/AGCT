@file:Suppress("PackageDirectoryMismatch")

package dsl

import model.utils.create
import model.variables.Concentration
import model.variables.Rate
import model.variables.Variable

@Suppress("UNCHECKED_CAST")
abstract class DslVariable internal constructor(private var privateValue: Variable<Number>) {
    protected fun<T : Variable<Number>> getCastedValue() =
        privateValue as T

    internal abstract val value: Variable<Number>

    infix fun of(value: Number) =
        into(sequenceOf(value))

    infix fun into(values: Sequence<Number>) {
        privateValue = privateValue::class.create(values)
    }
}

class DslConcentration internal constructor(default: Concentration = Concentration()) : DslVariable(default) {
    override val value: Concentration
        get() = getCastedValue()
}

class DslRate internal constructor(default: Rate = Rate()) : DslVariable(default) {
    override val value: Rate
        get() = getCastedValue()
}