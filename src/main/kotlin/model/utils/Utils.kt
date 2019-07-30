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
internal val Any.className
    get() = javaClass.simpleName
        ?.removePrefix("Basic")
        ?.capitalize()
        ?.split(('A'..'Z').toString())
        ?.joinToString(" ")
        ?: ""

/**
 * Given a [KClass] named 'Something', it returns the class named 'BasicSomething' if it exists.
 */
internal val KClass<*>.basicClass
    get() = Class.forName(buildString {
        append(qualifiedName)
        insert(lastIndexOf('.') + 1, "Basic")
    }).kotlin

/**
 * Given a [KClass] it returns its constructor or the constructor of its basic implementation if present.
 */
internal val KClass<*>.constructor
    get() = primaryConstructor ?: basicClass.primaryConstructor ?: throw IllegalStateException("$simpleName has no constructors")

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

/**
 * Given a [string][this], it is printed to output console.
 */
internal fun String.toConsole() = println(this)