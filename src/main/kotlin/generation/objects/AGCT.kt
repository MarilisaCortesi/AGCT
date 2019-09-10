@file:Suppress("PackageDirectoryMismatch")

package agct

import generation.Generator
import generation.utils.Level.Companion.start
import model.circuit.GeneticCircuit
import model.entities.GeneticEntity
import model.entities.BoundEntity
import model.entities.DegradingEntity
import model.entities.Gene
import model.entities.Protein
import model.entities.RegulatedGene
import model.entities.RegulatingEntity
import model.reactions.Degradation
import model.reactions.Regulation
import model.reactions.Transcription
import model.utils.UnsupportedClassException
import model.utils.forEachSelf
import model.utils.string
import model.utils.type
import model.variables.Variable

object AGCT : Generator {
    override fun from(circuit: GeneticCircuit) = with(circuit) {
        context = this
        start(" {", "}") {
            "import agct.*"()
            line()
            "fun main()" {
                "Create circuit ${name.string} containing" {
                    genes.forEachSelf {
                        transcriptions("the $entity that codes For", " and", -1) { transcription ->
                            "the ${transcription.target.entity}"()
                            "with a basal.rate ${transcription.basalRate.string}"()
                            transcription.regulations("regulated by", " and", -1) { regulation ->
                                "the ${regulation.regulator.entity}"()
                                "with a regulating.rate ${regulation.regulatingRate.string}"()
                                "with a binding.rate ${regulation.bindingRate.string}"()
                                "with an unbinding.rate ${regulation.unbindingRate.string}"()
                            }
                        }
                    }
                    line()
                    dslEntities({ "\"${it.id}\"" }, spacings = 1) { entity ->
                        "has an initial.concentration ${entity.initialConcentration.string}"()
                        if (entity is DegradingEntity) {
                            "has a degradation.rate ${entity.degradation.degradationRate.string}"()
                        }
                    }
                }
            }
        }.toFile("agct.kt", name)
        context = null
    }
}

private var context: GeneticCircuit? = null

private val GeneticCircuit.genes
    get() = entities.filterIsInstance<Gene>().filter { it !is RegulatedGene }

private val GeneticCircuit.dslEntities
    get() = entities.filter { it !is BoundEntity<*, *> }

private val GeneticEntity.entity
    get() = when (this) {
        is Gene -> "gene"
        is Protein -> "protein"
        is RegulatingEntity -> "molecule"
        else -> throw UnsupportedClassException(this)
    } + " ${id.string}"

private val DegradingEntity.degradation
    get() = context!!.reactionsOf(this).filterIsInstance<Degradation>().single()

private val Gene.transcriptions
    get() = context!!.reactionsOf(this).filterIsInstance<Transcription<*>>()

private val Transcription<*>.regulations
    get() = context!!.reactions.filterIsInstance<Regulation>().filter { it.reaction == this }

private val Variable<*>.string
    get() = if (values.size == 1) "of ${values.single()}"
            else "into ${values.joinToString(", ", "(", ")")}"
