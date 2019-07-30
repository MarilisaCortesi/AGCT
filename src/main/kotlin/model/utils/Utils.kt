package model.utils

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * Exception thrown when an unknown implementation of an biochemicalEntity or a biochemicalReaction is passed.
 * Likely to be used in a "when" construct.
 */
internal class UnknownClassException(
    message: String? = null,
    cause: Throwable? = null
) : IllegalArgumentException(message, cause)

/**
 * Returns the class name of an object.
 */
internal val Any?.className
    get() = this
        ?.javaClass
        ?.simpleName
        ?.removePrefix("Basic")
        ?.capitalize()
        ?.split(('A'..'Z').toString())
        ?.joinToString(" ")
        ?: ""

/**
 * Given an [object][this], it is printed to output console.
 */
internal fun Any?.toConsole() = println(this)

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