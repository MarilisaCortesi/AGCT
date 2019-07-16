package dsl.keywords.utils

/**
 * Defines the first wrapper that can be used sequentially with the function "and".
 *
 * @param T the type of the object to which will be applied the function of the wrapper.
 */
abstract class FirstTimeWrapper<in T> : KeywordWrapper {
    internal abstract fun performOn(obj: T)
}

/**
 * Defines the second wrapper that can be used sequentially with the function "and".
 *
 * @param T the type of the object to which will be applied the function of the wrapper.
 * @param S the type itself of the extended class, used to return a casted type.
 */
abstract class SecondTimeWrapper<in T, out S : SecondTimeWrapper<T, S>>(private val wrapped: FirstTimeWrapper<T>) :
    KeywordWrapper {
    @Suppress("UNCHECKED_CAST")
    infix fun and(obj: T) = apply { wrapped.performOn(obj) } as S
}