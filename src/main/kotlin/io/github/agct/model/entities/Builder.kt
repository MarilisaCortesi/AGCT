package io.github.agct.model.entities

import io.github.agct.model.utils.create
import io.github.agct.model.utils.lateVal
import io.github.agct.model.variables.BasicConcentration
import io.github.agct.model.variables.Concentration
import kotlin.reflect.KClass

/**
 * Creates an entity using its parameters and its [type][T].
 */
internal inline fun<reified T : Entity> entity(block: EntityParameters.() -> Unit = {}) =
    EntityParameters().apply(block).run { T::class.createEntity(this) }

/**
 * Creates an entity using its parameters and its [type][T].
 */
internal inline fun<reified T : Entity> entity(id: String, block: EntityParameters.() -> Unit = {}) =
    entity<T> {
        this.id = id
        block()
    }

/**
 * A class containing default parameters for a genetic entity.
 */
internal class EntityParameters {
    var id: String by lateVal()
    var initialConcentration: Concentration =
        BasicConcentration()
    var aliases: MutableList<String> = mutableListOf()
}


/**
 * Given a [KClass] named 'Something', it returns the class named 'BasicSomething' if it exists.
 */
@Suppress("UNCHECKED_CAST")
private fun<T : Entity> KClass<T>.createEntity(parameters: EntityParameters) =
    try {
        create(parameters)
    } catch (e: Exception) {
        Class.forName(buildString {
            append(qualifiedName?.replace("Genetic", ""))
            insert(lastIndexOf('.') + 1, "Basic")
        }).kotlin.create(parameters)
    } as T