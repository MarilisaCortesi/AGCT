package export.types

import export.ExportType
import export.utils.Level.Companion.start
import model.circuit.GeneticCircuit
import model.entities.BiochemicalEntity
import model.entities.BoundBiochemicalEntity

internal class Alchemist : ExportType {
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
                    "type"("Point()")
                    "parameters"("[0,0]")
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
    get() = entities.filter { it !is BoundBiochemicalEntity<*, *> }

private val GeneticCircuit.dslReactions
    get() = reactions.flatMap { it.reactions }

private val Map<BiochemicalEntity, Int>.string
    get() = entries.joinToString(" + ", "[", "]") {
        if (it.value != 1)
            "${it.value}${it.key.id}"
        else
            it.key.id
    }