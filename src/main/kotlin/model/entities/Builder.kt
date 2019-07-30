package model.entities

import model.variables.Concentration
import kotlin.properties.Delegates
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * Creates an biochemicalEntity using its parameters and its [type][T].
 */
internal inline fun<reified T : BiochemicalEntity> entity(block: EntityParameters.() -> Unit = {}) =
    EntityParameters().apply(block).run { T::class.constructor.call(this) } as T

/**
 * Creates an biochemicalEntity using its parameters and its [type][T].
 */
internal inline fun<reified T : BiochemicalEntity> entity(id: String, block: EntityParameters.() -> Unit = {}) =
    entity<T> {
        this.id = id
        block()
    }

/**
 * A class containing default parameters for a biochemical biochemicalEntity.
 */
internal class EntityParameters {
    var id: String by Delegates.notNull()
    var initialConcentration: Concentration = Concentration()
    var aliases: MutableList<String> = mutableListOf()
}


/**
 * Given a [KClass] named 'Something', it returns the class named 'BasicSomething' if it exists.
 */
private val KClass<*>.basicClass
    get() = Class.forName(buildString {
        append(qualifiedName?.replace("Biochemical", ""))
        insert(lastIndexOf('.') + 1, "Basic")
    }).kotlin

/**
 * Given a [KClass] it returns its constructor or the constructor of its basic implementation if present.
 */
private val KClass<*>.constructor
    get() = primaryConstructor ?: basicClass.primaryConstructor ?: throw IllegalStateException("$simpleName has no constructors")