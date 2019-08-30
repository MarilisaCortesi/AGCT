@file:Suppress("PackageDirectoryMismatch", "ClassName")

package dsl

import export.ExportType
import export.types.*
import model.circuit.GeneticCircuit

object export

object each

object into {
    operator fun invoke(vararg types: ExportObject) =
        types.toSet()
}

abstract class ExportObject internal constructor(internal val type: ExportType) {
    internal fun export(circuit: GeneticCircuit) = circuit.exportTo(type)
}

object entities: ExportObject(Entities())

object reactions: ExportObject(Reactions())

object AGCT: ExportObject(AGCT())

object Alchemist: ExportObject(Alchemist())