package model.entities

import model.utils.create
import model.variables.Concentration
import kotlin.properties.Delegates
import kotlin.reflect.KClass

/**
 * Creates an biochemicalEntity using its parameters and its [type][T].
 */
internal inline fun<reified T : BiochemicalEntity> entity(block: EntityParameters.() -> Unit = {}) =
    EntityParameters().apply(block).run { T::class.createEntity(this) }

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
    lateinit var id: String
    var initialConcentration: Concentration = Concentration()
    var aliases: MutableList<String> = mutableListOf()
}


/**
 * Given a [KClass] named 'Something', it returns the class named 'BasicSomething' if it exists.
 */
@Suppress("UNCHECKED_CAST")
private fun<T : BiochemicalEntity> KClass<T>.createEntity(parameters: EntityParameters) =
    try {
        create(parameters)
    } catch (e: Exception) {
        Class.forName(buildString {
            append(qualifiedName?.replace("Biochemical", ""))
            insert(lastIndexOf('.') + 1, "Basic")
        }).kotlin.create(parameters)
    } as T