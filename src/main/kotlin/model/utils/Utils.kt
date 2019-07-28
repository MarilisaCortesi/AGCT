package model.utils

/**
 * Exception thrown when an unknown implementation of an entity or a reaction is passed.
 * Likely to be used in a "when" construct.
 */
internal class UnknownClassException(
    message: String? = null,
    cause: Throwable? = null
) : IllegalArgumentException(message, cause)

/**
 * Returns the class name of an object.
 */
internal val Any.className
    get() = javaClass.simpleName
        ?.removePrefix("Basic")
        ?.capitalize()
        ?.split(('A'..'Z').toString())
        ?.joinToString(" ")
        ?: ""

/**
 * Returns true if the [other] object is the same as [this], false if not.
 * The type [C] is the type used to cast the [other] object.
 */
@Suppress("UNCHECKED_CAST")
internal fun<C : Any> C.checkEquals(other: Any?, check: (C) -> Boolean) =
    when {
        this === other -> true
        javaClass != other?.javaClass -> false
        else -> (other as C).let(check)
    }