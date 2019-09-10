@file:Suppress("PackageDirectoryMismatch")

package agct

import generation.Generator
import generation.utils.Level.Companion.start
import model.circuit.GeneticCircuit
import model.entities.GeneticEntity
import model.entities.BoundEntity

object Alchemist : Generator {
    override fun from(circuit: GeneticCircuit) = with(circuit) {
        start(null, null, "  ", ": ") {
            "incarnation"("biochemistry")
            line()
            "seeds:" {
                "scenario"(0)
                "simulation"(1)
            }
            line()
            "displacements:" {
                "- in:" {
                    "  type"("Point")
                    "  parameters"("[0, 0]")
                }
                "" {
                    "contents:" {
                        dslEntities.invoke({ "- molecule: ${it.id}" }) {
                            "concentration"(it.initialConcentration)
                        }
                    }
                    line()
                    "programs:" {
                        "- " {
                            dslReactions.invoke({ "- time-distribution: ${it.rate}" }) {
                                "program"("\"${it.reagents.string} --> ${it.products.string}\"")
                            }
                        }
                    }
                }
            }
        }.toFile("alchemist.yml", name)
    }
}

private val GeneticCircuit.dslEntities
    get() = entities.filter { it !is BoundEntity<*, *> }

private val GeneticCircuit.dslReactions
    get() = reactions.flatMap { it.reactions }

private val Map<GeneticEntity, Int>.string
    get() = entries.joinToString(" + ", "[", "]") {
        if (it.value != 1)
            "${it.value}${it.key.id}"
        else
            it.key.id
    }