package utils

import agct.AlchemistGenerator
import generation.defaultDirectory
import model.circuit.GeneticCircuit

class ExportableAlchemist(
    directoryPath: GeneticCircuit.() -> String = {
        "${System.getProperty("user.dir").replace("Examples", "")}/Examples/$defaultDirectory"
    },
    private val exportBlock: GeneticCircuit.() -> String
) : AlchemistGenerator(directoryPath) {
    override val files: GeneticCircuit.() -> Map<String, String> = {
        super.files(this).mapValues { (_, data) ->
            buildString {
                append(data)
                append("\n\n")
                append("export:")
                append("\n  ")
                append(exportBlock().replace("\n", "\n  "))
            }
        }
    }
}