package model.entities

import model.utils.constructor
import model.variables.Concentration
import kotlin.properties.Delegates

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