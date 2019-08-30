package model.circuit

import export.ExportType
import model.entities.*
import model.reactions.BiochemicalReaction

/**
 * Represents a [genetic geneticCircuit][GeneticCircuit] with a [name], its [entities] and the [reactions] between them.
 */
internal interface GeneticCircuit {
    val name: String

    val reactions: Set<BiochemicalReaction>
    val entities: Set<BiochemicalEntity>

    /**
     * Returns the reactions involving the given [entity].
     */
    fun reactionsOf(entity: BiochemicalEntity) : Set<BiochemicalReaction>

    /**
     * Adds an [entity] to the geneticCircuit.
     */
    fun addEntity(entity: BiochemicalEntity)

    /**
     * Adds a series of [entities] to the geneticCircuit.
     */
    fun addEntities(vararg entities: BiochemicalEntity)

    /**
     * Adds a [reaction] to the geneticCircuit.
     */
    fun addReaction(reaction: BiochemicalReaction)

    /**
     * Adds a series of [reactions] to the geneticCircuit.
     */
    fun addReactions(vararg reactions: BiochemicalReaction)

    /**
     * Exports the geneticCircuit.
     */
    fun exportTo(vararg types: ExportType)
}