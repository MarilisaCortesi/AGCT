@file:Suppress("PackageDirectoryMismatch")

package dsl

import model.utils.create
import model.utils.lateVal
import model.variables.*
import model.variables.BasicConcentration
import model.variables.BasicRate

abstract class DslVariable<N : Number> internal constructor(
    internal val variableName: String?,
    default: Variable<N>
) {
    protected var privateValue: Variable<N> by lateVal(variableName, default)

    internal abstract val value: Variable<N>

    infix fun of(value: N) =
        into(sequenceOf(value))

    infix fun into(values: Sequence<N>) {
        privateValue = privateValue::class.create(values)
    }
}

class DslConcentration internal constructor(
    variableName: String? = null,
    default: Concentration = BasicConcentration()
) : DslVariable<Int>(variableName, default) {
    override val value: Concentration
        get() = privateValue as Concentration
}

class DslRate internal constructor(
    variableName: String? = null,
    default: Rate = BasicRate()
) : DslVariable<Number>(variableName, default) {
    override val value: Rate
        get() = privateValue as Rate
}