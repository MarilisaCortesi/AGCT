package io.github.agct.model.circuit

import io.github.agct.model.entities.Entity
import io.github.agct.model.reactions.Reaction

/**
 * Represents a set of rules that must be followed by a [genetic circuit][GeneticCircuit]
 */
interface CircuitRules {
    /**
     * Contains the list of rules to be checked when adding a new reaction to the circuit.
     * A circuit must throw an [IllegalArgumentException] if one of them is not followed.
     */
    val addingRules: Map<String, (MutableSet<Reaction>, Entity, Reaction) -> Unit>

    /**
     * Contains the list of rules to be followed when exporting the circuit.
     * A circuit must throw an [IllegalStateException] if one of them are not followed.
     */
    val exportRules: Map<String, (Map<Entity, Set<Reaction>>) -> Unit>
}

/**
 * Represents a mutable set of [circuit rules][CircuitRules].
 */
internal interface MutableCircuitRules : CircuitRules {
    override val addingRules: MutableMap<String, (MutableSet<Reaction>, Entity, Reaction) -> Unit>

    override val exportRules: Map<String, (Map<Entity, Set<Reaction>>) -> Unit>

    operator fun String.invoke(addingRule: (MutableSet<Reaction>, Entity, Reaction) -> Unit)

    operator fun String.invoke(exportRule: (Map<Entity, Set<Reaction>>) -> Unit)
}

internal class BasicCircuitRules : MutableCircuitRules {
    override val addingRules = mutableMapOf<String, (MutableSet<Reaction>, Entity, Reaction) -> Unit>()

    override val exportRules = mutableMapOf<String, (Map<Entity, Set<Reaction>>) -> Unit>()

    override operator fun String.invoke(addingRule: (MutableSet<Reaction>, Entity, Reaction) -> Unit) {
        addingRules[this] = addingRule
    }

    override operator fun String.invoke(exportRule: (Map<Entity, Set<Reaction>>) -> Unit) {
        exportRules[this] = exportRule
    }
}
