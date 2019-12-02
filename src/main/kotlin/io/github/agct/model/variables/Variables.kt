package io.github.agct.model.variables

/**
 * A variable whose value can be found inside a set of [values].
 */
interface Variable<out T> {
    val values: Set<T>
}

/**
 * A variable that represents the concentration of an entity.
 */
interface Concentration : Variable<Int>

/**
 * A variable that represents the rate of a reaction.
 */
interface Rate : Variable<Double>

/**
 * Default value of [Concentration] variable.
 */
internal const val DEFAULT_CONCENTRATION_VALUE = 100

/**
 * Default value of [Rate] variable.
 */
const val DEFAULT_RATE_VALUE = 1.0