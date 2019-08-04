package model.variables

import model.utils.checkEquals
import model.utils.type

/**
 * A variable whose value can be found inside a set of [values].
 */
internal interface Variable<out T> {
    val values: Set<T>
}

@Suppress("UNCHECKED_CAST")
internal abstract class PositiveVariable<out T : Number>(values: Sequence<T>) : Variable<T> {
    init {
        for (value in values) {
            if (value.toDouble() <= 0)
                throw IllegalArgumentException("Variable must have positive values only.")
        }
    }

    override val values = values.map { it }.toSet()

    override fun toString() =
        "$type(${values.joinToString { ", " }})"

    override fun hashCode() =
        values.hashCode()

    override fun equals(other: Any?) =
        checkEquals(other) { values == it.values }
}

/**
 * Default value of [Rate] variable.
 */
internal const val DEFAULT_RATE_VALUE = 1.0

internal class Rate(values: Sequence<Number>) : PositiveVariable<Double>(values.map { it.toDouble() }) {
    constructor(values: Collection<Number>) : this(values.asSequence())
    constructor(vararg values: Number = arrayOf(DEFAULT_RATE_VALUE)) : this(values.asSequence())
}

/**
 * Default value of [Concentration] variable.
 */
internal const val DEFAULT_CONCENTRATION_VALUE = 100.0

internal class Concentration(values: Sequence<Number>) : PositiveVariable<Double>(values.map { it.toDouble() }) {
    constructor(values: Collection<Number>) : this(values.asSequence())
    constructor(vararg values: Number = arrayOf(DEFAULT_CONCENTRATION_VALUE)) : this(values.asSequence())
}