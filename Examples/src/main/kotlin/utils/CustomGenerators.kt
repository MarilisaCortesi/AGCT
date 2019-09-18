package utils

import agct.AlchemistGenerator
import generation.defaultDirectory
import model.circuit.GeneticCircuit

class ExportableAlchemist(
    directoryPath: GeneticCircuit.() -> String = {
        "${System.getProperty("user.dir").replace("Examples", "")}/Examples/$defaultDirectory/alchemist.yml"
    },
    exportBlock: StringBuilder.(GeneticCircuit) -> Unit
) : AlchemistGenerator(directoryPath) {
    private val block: GeneticCircuit.() -> String = { StringBuilder().let { it.exportBlock(this) }.toString() }

    override val files: GeneticCircuit.() -> Map<String, String> = {
        super.files(this).mapValues { (_, data) ->
            buildString {
                append(data)
                append("\n\n")
                append("export:")
                append("\n  ")
                append(block().replace("\n", "\n  "))
            }
        }
    }
}

fun StringBuilder.line(line: String): StringBuilder =
    append("$line\n")