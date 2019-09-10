@file:Suppress("PackageDirectoryMismatch", "ClassName")

package agct

import kotlin.math.log10
import kotlin.math.pow

private fun sequence(from: Number, step: Number, num: Int): Sequence<Number> =
    generateSequence(0) { it + 1 }.map { it * step.toDouble() + from.toDouble() }.take(num)

interface NumericSequence

object values : NumericSequence {
    /**
     * A custom range of arbitrary [values].
     */
    operator fun invoke(vararg values: Number) : Sequence<Number> =
        values.map { it.toDouble() }.asSequence()
}

object range : NumericSequence {
    /**
     * An inclusive range of [values].
     * Starting [from] and ending [to] the given values, with the given [step].
     */
    operator fun invoke(from: Number, to: Number, step: Number = 1.0) : Sequence<Number> =
        sequence(from, step, ((to.toDouble() - from.toDouble()) / step.toDouble()).toInt() + 1)
}

object linspace : NumericSequence {
    /**
     * An inclusive range of evenly spaced [values].
     * Starting [from] and ending [to] the given values, for a total of [num] elements.
     */

    operator fun invoke(from: Number, to: Number, num: Int) : Sequence<Number> =
        sequence(from, (to.toDouble() - from.toDouble()) / (num - 1), num)
}

object logspace : NumericSequence {
    /**
     * An inclusive range of [values] spaced evenly on a log scale.
     * Starting [from] and ending [to] the given values, for a total of [num] elements.
     */
    operator fun invoke(from: Number, to: Number, num: Int) : Sequence<Number> =
        linspace(from, to, num).map { 10.0.pow(it.toDouble()) }
}

object geomspace : NumericSequence {
    /**
     * An inclusive range of [values] spaced evenly on a log scale (a geometric progression).
     * Starting [from] and ending [to] the given values, for a total of [num] elements.
     */
    operator fun invoke(from: Number, to: Number, num: Int) : Sequence<Number> =
        logspace(log10(from.toDouble()), log10(to.toDouble()), num)
}