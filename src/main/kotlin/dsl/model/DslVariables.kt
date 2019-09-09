@file:Suppress("PackageDirectoryMismatch")

package dsl

import model.utils.create
import model.utils.lateVal
import model.variables.*
import model.variables.BasicConcentration
import model.variables.BasicRate

@Suppress("UNCHECKED_CAST")
abstract class DslVariable internal constructor(
    internal val variableName: String?,
    default: Variable<Number>
) {
    // being protected, it would expose internal class Variable<Number>
    // to avoid this, the value must remain private and be passed through the function getCastedValue
    private var privateValue: Variable<Number> by lateVal(variableName, default)

    protected fun<T : Variable<Number>> getCastedValue() =
        privateValue as T

    internal abstract val value: Variable<Number>

    infix fun of(value: Number) =
        into(sequenceOf(value))

    infix fun into(values: Sequence<Number>) {
        privateValue = privateValue::class.create(values)
    }
}

class DslConcentration internal constructor(
    variableName: String? = null,
    default: Concentration = BasicConcentration()
) : DslVariable(variableName, default) {
    override val value: Concentration
        get() = getCastedValue()
}

class DslRate internal constructor(
    variableName: String? = null,
    default: Rate = BasicRate()
) : DslVariable(variableName, default) {
    override val value: Rate
        get() = getCastedValue()
}