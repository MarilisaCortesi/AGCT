package io.github.agct.examples.utils

import io.github.agct.generation.defaultDirectory
import io.github.agct.generation.target.AlchemistGenerator
import io.github.agct.model.circuit.GeneticCircuit

class ExportableAlchemist(
    directoryPath: GeneticCircuit.() -> String = { "$defaultDirectory/alchemist.yml" },
    exportBlock: StringBuilder.(GeneticCircuit) -> Unit = { }
) : AlchemistGenerator(directoryPath) {
    private val block: GeneticCircuit.() -> String = { StringBuilder().also { it.exportBlock(this) }.toString() }

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