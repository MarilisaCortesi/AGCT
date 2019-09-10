@file:Suppress("PackageDirectoryMismatch", "ClassName")

package agct

import kotlin.math.log10
import kotlin.math.pow

private fun sequence(from: Number, step: Number, num: Int) =
    generateSequence(0) { it + 1 }.map { it * step.toDouble() + from.toDouble() }.take(num)

interface NumericSequence

object values : NumericSequence {
    /**
     * A custom range of arbitrary [values].
     */
    operator fun invoke(vararg values: Number) =
        values.map { it.toDouble() }.asSequence()
}

object range : NumericSequence {
    /**
     * An inclusive range of [values].
     * Starting [from] and ending [to] the given values, with the given [step].
     */
    operator fun invoke(from: Number, to: Number, step: Number = 1.0) =
        sequence(from, step, ((to.toDouble() - from.toDouble()) / step.toDouble()).toInt() + 1)
}

object linspace : NumericSequence {
    /**
     * An inclusive range of equally spaced [values].
     * Starting [from] and ending [to] the given values, for a total of [num] elements.
     */

    operator fun invoke(from: Number, to: Number, num: Int) =
        sequence(from, (to.toDouble() - from.toDouble()) / (num - 1), num)
}

object logspace : NumericSequence {
    /**
     * An inclusive range of [values] spaced on a logarithmic scale.
     * Starting [from] and ending [to] the given values, for a total of [num] elements.
     */
    operator fun invoke(from: Number, to: Number, num: Int) =
        linspace(from, to, num).map { 10.0.pow(it) }
}

object geomspace : NumericSequence {
    /**
     * An inclusive range of [values] spaced on a geometrical scale.
     * Starting [from] and ending [to] the given values, for a total of [num] elements.
     */
    operator fun invoke(from: Number, to: Number, num: Int) =
        logspace(log10(from.toDouble()), log10(to.toDouble()), num)
}