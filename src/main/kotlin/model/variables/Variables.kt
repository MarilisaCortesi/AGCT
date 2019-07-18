package model.variables

/**
 * A variable whose value can be found inside a set of [values].
 */
interface Variable<T> {
    val values: Set<T>
}

@Suppress("UNCHECKED_CAST")
abstract class AbstractVariable<T> : Variable<T> {
    override fun hashCode() = values.hashCode()

    override fun equals(other: Any?) =
        when {
            this === other -> true
            other?.javaClass != javaClass -> false
            else -> (other as Variable<T>).values == values
        }
}

/**
 * Default value of [Rate] variable.
 */
const val DEFAULT_RATE_VALUE = 1.0

class Rate internal constructor (values: Collection<Number>): AbstractVariable<Double>() {
    internal constructor(values: Sequence<Number>) : this(values.toSet())
    internal constructor(vararg values: Number = arrayOf(DEFAULT_RATE_VALUE)) : this(values.toSet())

    override val values: Set<Double> = values.map { it.toDouble() }.toSet()
}

/**
 * Default value of [Concentration] variable.
 */
const val DEFAULT_CONCENTRATION_VALUE = 100.0

class Concentration internal constructor (values: Collection<Number>): AbstractVariable<Double>() {
    internal constructor(values: Sequence<Number>) : this(values.toSet())
    internal constructor(vararg values: Number = arrayOf(DEFAULT_CONCENTRATION_VALUE)) : this(values.toSet())

    override val values: Set<Double> = values.map { it.toDouble() }.toSet()
}