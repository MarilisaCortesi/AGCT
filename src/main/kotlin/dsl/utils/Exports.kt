@file:Suppress("PackageDirectoryMismatch", "ClassName")

package dsl

import model.circuit.export.ExportTypes
import model.circuit.GeneticCircuit

fun all(vararg types: ExportObject) = types.toSet()

abstract class ExportObject internal constructor() {
    internal abstract val type: ExportTypes
    internal fun export(circuit: GeneticCircuit) = circuit.exportTo(type)
}

object entities: ExportObject() {
    override val type = ExportTypes.Entities
}

object reactions: ExportObject() {
    override val type = ExportTypes.Reactions
}

object AGCT: ExportObject() {
    override val type = ExportTypes.AGCT
}

object Alchemist: ExportObject() {
    override val type = ExportTypes.Alchemist
}