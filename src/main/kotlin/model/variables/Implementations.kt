package model.variables

import model.utils.checkEquals

@Suppress("UNCHECKED_CAST")
internal abstract class UnsignedVariable<out T : Number>(values: Sequence<T>) : Variable<T> {
    init {
        for (value in values) {
            require(value.toDouble() >= 0) { "Variable must have non-negative values only." }
        }
    }

    override val values = values.toSet()

    override fun toString() = values.run {
        if (size == 1)
            single().toString()
        else
            joinToString(", ", "(", ")")
    }

    override fun hashCode() =
        values.hashCode()

    override fun equals(other: Any?) =
        checkEquals(other) { values == it.values }
}

internal class BasicRate(values: Sequence<Number>) :
    UnsignedVariable<Number>(values), Rate {
    constructor(values: Collection<Number>) : this(values.asSequence())
    constructor(vararg values: Number = arrayOf(DEFAULT_RATE_VALUE)) : this(values.asSequence())
}

internal class BasicConcentration(values: Sequence<Int>) :
    UnsignedVariable<Int>(values), Concentration {
    constructor(values: Collection<Int>) : this(values.asSequence())
    constructor(vararg values: Int = intArrayOf(DEFAULT_CONCENTRATION_VALUE)) : this(values.asSequence())
}