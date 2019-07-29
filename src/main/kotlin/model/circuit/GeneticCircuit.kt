package model.circuit

import model.entities.*
import model.reactions.BiochemicalReaction

/**
 * Represents a [genetic geneticCircuit][GeneticCircuit] with a [name], its [entities] and the [reactions] between them.
 */
internal interface GeneticCircuit {
    val name: String
    val entities: Set<BiochemicalEntity>
    val reactions: Set<BiochemicalReaction>

    /**
     * Adds a series of [reactions] to the geneticCircuit.
     */
    fun add(vararg reactions: BiochemicalReaction)

    /**
     * Exports the geneticCircuit.
     */
    fun exportTo(vararg types: ExportTypes)
}