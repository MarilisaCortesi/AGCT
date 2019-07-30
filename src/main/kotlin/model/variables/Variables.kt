package model.variables

import model.utils.checkEquals
import model.utils.className

/**
 * A variable whose value can be found inside a set of [values].
 */
internal interface Variable<out T> {
    val values: Set<T>
}

@Suppress("UNCHECKED_CAST")
internal abstract class PositiveVariable<out T : Number>(values: Collection<T>) : Variable<T> {
    init {
        for (value in values) {
            if (value.toDouble() <= 0)
                throw IllegalArgumentException("Variable must have positive values only.")
        }
    }

    override val values = values.map { it }.toSet()

    override fun toString() =
        "$className(${values.joinToString { ", " }})"

    override fun hashCode() =
        values.hashCode()

    override fun equals(other: Any?) =
        checkEquals(other) { values == it.values }
}

/**
 * Default value of [Rate] variable.
 */
internal const val DEFAULT_RATE_VALUE = 1.0

internal class Rate(values: Collection<Number>): PositiveVariable<Double>(values.map { it.toDouble() }) {
    constructor(values: Sequence<Number>) : this(values.toSet())
    constructor(vararg values: Number = arrayOf(DEFAULT_RATE_VALUE)) : this(values.toSet())
}

/**
 * Default value of [Concentration] variable.
 */
internal const val DEFAULT_CONCENTRATION_VALUE = 100.0

internal class Concentration(values: Collection<Number>): PositiveVariable<Double>(values.map { it.toDouble() }) {
    constructor(values: Sequence<Number>) : this(values.toSet())
    constructor(vararg values: Number = arrayOf(DEFAULT_CONCENTRATION_VALUE)) : this(values.toSet())
}