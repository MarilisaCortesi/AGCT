package model.entities

import model.utils.UnknownClassException
import model.variables.Concentration
import kotlin.properties.Delegates
import kotlin.reflect.full.primaryConstructor

/**
 * Creates an entity using its parameters and its [type][T].
 */
internal inline fun<reified T : BiochemicalEntity> entity(block: EntityParameters.() -> Unit = {}) =
    EntityParameters().apply(block).run {
        T::class.primaryConstructor?.call(this) ?: when {
            T::class == Molecule::class -> BasicMolecule(this)
            T::class == DegradingMolecule::class -> BasicDegradingMolecule(this)
            T::class == RegulatingMolecule::class -> BasicRegulatingMolecule(this)
            T::class == Gene::class -> BasicGene(this)
            T::class == MRna::class -> BasicMRna(this)
            T::class == Protein::class -> BasicProtein(this)
            else -> throw UnknownClassException("${T::class} cannot be created with this builder.")
        } as T
    }

/**
 * Creates an entity using its parameters and its [type][T].
 */
internal inline fun<reified T : BiochemicalEntity> entity(id: String, block: EntityParameters.() -> Unit = {}) =
    entity<T> {
        this.id = id
        block()
    }

/**
 * A class containing default parameters for a biochemical entity.
 */
internal class EntityParameters {
    var id: String by Delegates.notNull()
    var initialConcentration: Concentration = Concentration()
    var aliases: MutableList<String> = mutableListOf()
}