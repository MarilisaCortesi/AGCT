@file:Suppress("PackageDirectoryMismatch")

package agct

import io.github.agct.model.utils.lateVal
import io.github.agct.model.variables.BasicConcentration
import io.github.agct.model.variables.BasicRate
import io.github.agct.model.variables.Concentration
import io.github.agct.model.variables.Rate
import io.github.agct.model.variables.Variable

abstract class DslVariable<N : Number> internal constructor(
    internal val variableName: String?,
    default: Variable<N>
) {
    protected var privateValue: Variable<N> by lateVal(
        variableName,
        default
    )

    internal abstract val value: Variable<N>

    protected abstract fun Sequence<Number>.set(): Variable<N>

    infix fun of(value: N) =
        into(sequenceOf(value))

    infix fun into(values: Sequence<Number>) {
        privateValue = values.set()
    }
}

class DslConcentration internal constructor(
    variableName: String? = null,
    default: Concentration = BasicConcentration()
) : DslVariable<Int>(variableName, default) {
    override val value: Concentration
        get() = privateValue as Concentration

    override fun Sequence<Number>.set(): Variable<Int> =
        BasicConcentration(map { it.toInt() })
}

class DslRate internal constructor(
    variableName: String? = null,
    default: Rate = BasicRate()
) : DslVariable<Number>(variableName, default) {
    override val value: Rate
        get() = privateValue as Rate

    override fun Sequence<Number>.set(): Variable<Double> =
        BasicRate(map { it.toDouble() })
}