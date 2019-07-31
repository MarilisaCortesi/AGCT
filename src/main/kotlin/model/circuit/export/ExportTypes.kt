package model.circuit.export

import model.circuit.GeneticCircuit
import model.utils.toConsole
internal enum class ExportTypes(val from: GeneticCircuit.() -> Unit) {
    Entities({
        entities.joinToString("\n", "ENTITIES: \n", "\n").toConsole()
    }),
    Reactions({
        reactions.map { it.reactions }.flatten().joinToString("\n", "REACTIONS: \n", "\n").toConsole()
    }),
    AGCT({
        agctExport()
    }),
    Alchemist({
        throw NotImplementedError("$this")
    });
}