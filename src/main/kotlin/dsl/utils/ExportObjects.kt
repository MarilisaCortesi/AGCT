@file:Suppress("ClassName")

package dsl.utils

import model.circuit.export.ExportTypes
import model.circuit.GeneticCircuit

abstract class ExportObject internal constructor() {
    internal abstract val type: ExportTypes
    internal fun export(circuit: GeneticCircuit) = circuit.exportTo(type)
}

object entityList: ExportObject() {
    override val type = ExportTypes.Entities
}

object reactionList: ExportObject() {
    override val type = ExportTypes.Reactions
}

object AGCT: ExportObject() {
    override val type = ExportTypes.AGCT
}

object Alchemist: ExportObject() {
    override val type = ExportTypes.Alchemist
}