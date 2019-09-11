@file:Suppress("PackageDirectoryMismatch")

package agct

import generation.AbstractGenerator
import generation.utils.Level.Companion.start
import model.circuit.GeneticCircuit
import model.entities.GeneticEntity
import model.entities.BoundEntity

object Alchemist : AlchemistGenerator()

open class AlchemistGenerator : AbstractGenerator({ file ->
    val variables = mutableListOf<Variable<*>>(*dslConcentrations.toTypedArray(), *dslRates.toTypedArray())

    file["export/${name.toLowerCase()}/alchemist.yml"] =
        start(prefix = null, postfix = null, indentation = "  ", stringSeparator = ": ") { // Level
            "incarnation"("biochemistry")
            line()
            "variables: " { // Level
                variables({"${it.id}: &${it.id}"}) { // Level
                    "type"("ArbitraryVariable")
                    "parameters"("[${it.values.first()}, ${it.values.joinToString(separator = ", ")}]")
                }
            }
            line()
            "seeds:" { // Level
                "scenario"(0)
                "simulation"(1)
            }
            line()
            "displacements:" { // Level
                "- in:" { // Level
                    "  type"("Point")
                    "  parameters"("[0, 0]")
                }
                "" { // Level
                    "contents:" { // Level
                        dslConcentrations(line = { "- molecule: ${it.info.molecule}" }) { // Level
                            "concentration"("*${it.id}")
                        }
                    }
                    line()
                    "programs:" { // Level
                        "- " { // Level
                            dslRates(line = { "- time-distribution: *${it.id}" }) { // Level
                                "program"(it.info.program)
                            }
                        }
                    }
                }
            }
        }.toString()
})

private data class Variable<I : Any>(val id: String, val values: Collection<*>, val info: I)

private val GeneticCircuit.dslConcentrations
    get() = entities.filter { it !is BoundEntity<*, *> }.mapIndexed { index, entity ->
        Variable("conc$index", entity.initialConcentration.values, object {
            val molecule = entity.id
        })
    }

private val GeneticCircuit.dslRates
    get() = reactions.flatMap { it.reactions }.mapIndexed { index, reaction ->
        Variable("rate$index", reaction.rate.values, object {
            val program = "\"${reaction.reagents.string} --> ${reaction.products.string}\""
        })
    }

private val Map<GeneticEntity, Int>.string
    get() = entries.joinToString(" + ", "[", "]") {
        if (it.value != 1)
            "${it.value}${it.key.id}"
        else
            it.key.id
    }