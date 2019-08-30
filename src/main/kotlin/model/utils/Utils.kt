package model.utils

import com.sun.org.apache.xpath.internal.operations.Bool
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Exception thrown when an unsupported class is passed.
 * Likely to be used in a "when" construct.
 */
internal class UnsupportedClassException(unsupported: Any?) :
    IllegalArgumentException("$unsupported has class ${unsupported?.javaClass}, which is not supported in this context.")

/**
 * Given an [object][this], it is printed to output console.
 */
internal fun Any?.toConsole() = println(this)

/**
 * Returns the type of an object in form of a string.
 */
internal val Any?.type
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
 * Creates an instance from a [class][this] using the given [parameters].
 */
internal fun<T : Any> KClass<T>.create(vararg parameters: Any?) : T {
    for (constructor in constructors) {
        try {
            return constructor.call(*parameters)
        } catch (e: Exception) { }
    }
    throw IllegalArgumentException("$this cannot be created with the given parameters.")
}

/**
 * Returns true if the [other] object is the same as [this], false if not.
 * The type [T] is the type used to cast the [other] object.
 */
@Suppress("UNCHECKED_CAST")
internal fun<T : Any> T.checkEquals(other: Any?, check: (T) -> Boolean) =
    when {
        this === other -> true
        javaClass != other?.javaClass -> false
        else -> (other as T).let(check)
    }

/**
 * Custom delegate to perform late initialization on a var just once.
 */
internal fun <T : Any> lateVal(propertyName: String? = null, temporaryValue: T? = null) =
    object : ReadWriteProperty<Any?, T> {
        private var value: T? = null

        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return value ?:
                   temporaryValue ?:
                   throw IllegalStateException("The ${propertyName ?: property.name} has not been set yet.")
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            if (this.value == null)
                this.value = value
            else
                throw IllegalStateException("The ${propertyName ?: property.name} has been already set.")
        }
    }

/**
 * Custom delegate to have an optional value throwing [NullPointerException] when trying to access it if no value is set
 */
internal class Nullable<T : Any> (private var innerValue: T? = null) {
    val get: T
        get() = innerValue ?: throw NullPointerException("$this has no value.")

    fun set(value: T?) {
        innerValue = value
    }
}

/**
 * Surrounds the string with two inverted commas to resemble a string.
 */
internal val String.string
    get() = "\"$this\""