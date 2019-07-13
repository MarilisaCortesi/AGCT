package model.variables

import kotlin.math.log10
import kotlin.math.pow

private fun sequence(from: Number, step: Number, num: Int) =
    generateSequence(0) { it + 1 }.map { it * step.toDouble() + from.toDouble() }.take(num)

/**
 * A custom range of arbitrary values.
 *
 * @param values the custom values
 */
fun values(vararg values: Number) =
    values.map { it.toDouble() }.asSequence()

/**
 * An inclusive range of values.
 *
 * @param from the starting value
 * @param to the ending value
 * @param step the step from a value to the following one
 */
fun range(from: Number, to: Number, step: Number = 1.0) =
    sequence(from, step, ((to.toDouble() - from.toDouble()) / step.toDouble()).toInt() + 1)

/**
 * An inclusive range of equally spaced values.
 *
 * @param from the starting value
 * @param to the ending value
 * @param num the total number of values
 */
fun linspace(from: Number, to: Number, num: Int) =
    sequence (from, (to.toDouble() - from.toDouble()) / (num - 1), num)

/**
 * An inclusive range of values spaced on a logarithmic scale.
 *
 * @param from the starting value
 * @param to the ending value
 * @param num the total number of values
 */
fun logspace(from: Number, to: Number, num: Int) =
    linspace(from, to, num).map { 10.0.pow(it) }

/**
 * An inclusive range of values spaced on a geometrical scale.
 *
 * @param from the starting value
 * @param to the ending value
 * @param num the total number of values
 */
fun geomspace(from: Number, to: Number, num: Int) =
    logspace(log10(from.toDouble()), log10(to.toDouble()), num)