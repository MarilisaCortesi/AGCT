package model.entities

import model.utils.create
import model.utils.lateVal
import model.variables.BasicConcentration
import model.variables.Concentration
import kotlin.reflect.KClass

/**
 * Creates an entity using its parameters and its [type][T].
 */
internal inline fun<reified T : GeneticEntity> entity(block: EntityParameters.() -> Unit = {}) =
    EntityParameters().apply(block).run { T::class.createEntity(this) }

/**
 * Creates an entity using its parameters and its [type][T].
 */
internal inline fun<reified T : GeneticEntity> entity(id: String, block: EntityParameters.() -> Unit = {}) =
    entity<T> {
        this.id = id
        block()
    }

/**
 * A class containing default parameters for a genetic entity.
 */
internal class EntityParameters {
    var id: String by lateVal()
    var initialConcentration: Concentration = BasicConcentration()
    var aliases: MutableList<String> = mutableListOf()
}


/**
 * Given a [KClass] named 'Something', it returns the class named 'BasicSomething' if it exists.
 */
@Suppress("UNCHECKED_CAST")
private fun<T : GeneticEntity> KClass<T>.createEntity(parameters: EntityParameters) =
    try {
        create(parameters)
    } catch (e: Exception) {
        Class.forName(buildString {
            append(qualifiedName?.replace("Genetic", ""))
            insert(lastIndexOf('.') + 1, "Basic")
        }).kotlin.create(parameters)
    } as T