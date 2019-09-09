package model.circuit

import model.entities.GeneticEntity
import model.reactions.GeneticReaction

/**
 * Represents a set of rules that must be followed by a [genetic circuit][GeneticCircuit]
 */
interface CircuitRules {
    /**
     * Contains the list of rules to be checked when adding a new reaction to the circuit.
     * A circuit must throw an [IllegalArgumentException] if one of them is not followed.
     */
    val addingRules: Map<String, (MutableSet<GeneticReaction>, GeneticEntity, GeneticReaction) -> Unit>

    /**
     * Contains the list of rules to be followed when exporting the circuit.
     * A circuit must throw an [IllegalStateException] if one of them are not followed.
     */
    val exportRules: Map<String, (Map<GeneticEntity, Set<GeneticReaction>>) -> Unit>
}

/**
 * Represents a mutable set of [circuit rules][CircuitRules].
 */
internal interface MutableCircuitRules : CircuitRules {
    override val addingRules: MutableMap<String, (MutableSet<GeneticReaction>, GeneticEntity, GeneticReaction) -> Unit>

    override val exportRules: Map<String, (Map<GeneticEntity, Set<GeneticReaction>>) -> Unit>

    operator fun String.invoke(addingRule: (MutableSet<GeneticReaction>, GeneticEntity, GeneticReaction) -> Unit)

    operator fun String.invoke(exportRule: (Map<GeneticEntity, Set<GeneticReaction>>) -> Unit)
}

internal class BasicCircuitRules : MutableCircuitRules {
    override val addingRules = mutableMapOf<String, (MutableSet<GeneticReaction>, GeneticEntity, GeneticReaction) -> Unit>()

    override val exportRules = mutableMapOf<String, (Map<GeneticEntity, Set<GeneticReaction>>) -> Unit>()

    override operator fun String.invoke(addingRule: (MutableSet<GeneticReaction>, GeneticEntity, GeneticReaction) -> Unit) {
        addingRules[this] = addingRule
    }

    override operator fun String.invoke(exportRule: (Map<GeneticEntity, Set<GeneticReaction>>) -> Unit) {
        exportRules[this] = exportRule
    }
}
