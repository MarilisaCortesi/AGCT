package model.variables

/**
 * A variable whose value can be found inside a set of values.
 *
 * @property values the set of values
 */
interface Variable<T> {
    val values: Set<T>
}

/**
 * An abstract variable with default implementation.
 */
abstract class AbstractVariable<T> : Variable<T> {
    override fun hashCode() = values.hashCode()

    override fun equals(other: Any?) =
        if (this === other) true
        else if (other?.javaClass != javaClass) false
        else {
            @Suppress("UNCHECKED_CAST")
            (other as Variable<T>).values == values
        }
}

/**
 * Default value of rate variable
 */
const val DEFAULT_RATE_VALUE = 1.0

/**
 * Contains one or more values that will be tested as the rate of a reaction.
 */
class Rate internal constructor (values: Collection<Number>): AbstractVariable<Double>() {
    internal constructor(values: Sequence<Number>) : this(values.toSet())
    internal constructor(vararg values: Number = arrayOf(DEFAULT_RATE_VALUE)) : this(values.toSet())

    override val values: Set<Double> = values.map { it.toDouble() }.toSet()
}

/**
 * Default value of concentration variable
 */
const val DEFAULT_CONCENTRATION_VALUE = 100.0

/**
 * Contains one or more values that will be tested as the concentration of a molecule.
 */
class Concentration internal constructor (values: Collection<Number>): AbstractVariable<Double>() {
    internal constructor(values: Sequence<Number>) : this(values.toSet())
    internal constructor(vararg values: Number = arrayOf(DEFAULT_CONCENTRATION_VALUE)) : this(values.toSet())

    override val values: Set<Double> = values.map { it.toDouble() }.toSet()
}