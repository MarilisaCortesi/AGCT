package dsl.keywords.utils

/**
 * Defines an object the wraps the logic of a keyword.
 */
interface KeywordWrapper

/**
 * Defines a keyword that can be wrapped.
 *
 * @param B the type of the object used before the keyword (before type)
 * @param A the type of the wrapping object (after type)
 */
abstract class WrappableKeyword<in B, out A : KeywordWrapper>(private val wrapperConstructor: (B) -> A) {
    // this is an abstract class and not an interface as interfaces can't have internal functions
    internal fun wrapper(obj: B) : A = wrapperConstructor(obj)
}