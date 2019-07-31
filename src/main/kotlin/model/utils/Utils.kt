package model.utils

/**
 * Exception thrown when an unsupported class is passed.
 * Likely to be used in a "when" construct.
 */
internal class UnsupportedClassException(unsupported: Any?) :
    IllegalArgumentException("$unsupported has class ${unsupported?.javaClass}, which is not supported in this context.")

/**
 * Returns the class name of an object.
 */
internal val Any?.className
    get() = this
        ?.javaClass
        ?.simpleName
        ?.removePrefix("Basic")
        ?.removePrefix("Dsl")
        ?.capitalize()
        ?.split(('A'..'Z').toString())
        ?.joinToString(" ")
        ?: ""

/**
 * A for each method with a "this" context for each element.
 */
internal fun<T : Any?> Collection<T>.forEachSelf(action: T.() -> Unit) =
    forEach { it.action() }

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

/**
 * Surrounds the string with two inverted commas to resemble a string.
 */
internal val String.string
    get() = "\"$this\""