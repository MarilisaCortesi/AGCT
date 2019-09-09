package model.circuit

import model.entities.GeneticEntity
import model.reactions.GeneticReaction

internal class BasicCircuitRules : CircuitRules {
    override operator fun String.invoke(addingRule: (MutableSet<GeneticReaction>, GeneticEntity, GeneticReaction) -> Unit) {
        addingRules[this] = addingRule
    }

    override operator fun String.invoke(exportRule: (Map<GeneticEntity, Set<GeneticReaction>>) -> Unit) {
        exportRules[this] = exportRule
    }


    override val addingRules = mutableMapOf<String, (MutableSet<GeneticReaction>, GeneticEntity, GeneticReaction) -> Unit>()

    override val exportRules = mutableMapOf<String, (Map<GeneticEntity, Set<GeneticReaction>>) -> Unit>()
}