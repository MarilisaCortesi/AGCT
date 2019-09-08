package generation.objects

import generation.Generator
import generation.utils.Level.Companion.start
import model.circuit.GeneticCircuit
import model.entities.BiochemicalEntity
import model.entities.BoundBiochemicalEntity
import model.entities.DegradingEntity
import model.entities.Gene
import model.entities.Protein
import model.entities.RegulatedGene
import model.entities.RegulatingEntity
import model.reactions.Degradation
import model.reactions.Regulation
import model.reactions.Transcription
import model.utils.Nullable
import model.utils.UnsupportedClassException
import model.utils.string

object AGCT : Generator {
    override fun from(circuit: GeneticCircuit) = with(circuit) {
        context.set(this)
        start(" {", "}") {
            "import dsl.*"()
            line()
            "fun main()" {
                "Create circuit ${name.string} containing" {
                    genes.invoke({ "the ${it.entity} that" }, spacings = 1) { gene ->
                        gene.transcriptions.invoke("codes For", " and", -1) { transcription ->
                            "the ${transcription.target.entity}"()
                            "with a basal.rate ${transcription.basalRate}"()
                            transcription.regulations.invoke("regulated by", " and", -1) { regulation ->
                                "the ${regulation.regulator.entity}"()
                                "with a regulating.rate ${regulation.regulatingRate}"()
                                "with a binding.rate ${regulation.bindingRate}"()
                                "with an unbinding.rate ${regulation.unbindingRate}"()
                            }
                        }
                    }
                    line()
                    dslEntities({ "\"${it.id}\"" }, spacings = 1) { entity ->
                        "has an initial.concentration ${entity.initialConcentration}"()
                        if (entity is DegradingEntity) {
                            "has a degradation.rate ${entity.degradation.degradationRate}"()
                        }
                    }
                }
            }
        }.toFile("agct.kt", name)
        context.set(null)
    }
}

private val context = Nullable<GeneticCircuit>()

private val GeneticCircuit.genes
    get() = entities.filterIsInstance<Gene>().filter { it !is RegulatedGene }

private val GeneticCircuit.dslEntities
    get() = entities.filter { it !is BoundBiochemicalEntity<*, *> }

private val BiochemicalEntity.entity
    get() = when (this) {
        is Gene -> "gene"
        is Protein -> "protein"
        is RegulatingEntity -> "molecule"
        else -> throw UnsupportedClassException(this)
    } + " ${id.string}"

private val DegradingEntity.degradation
    get() = context.get.reactionsOf(this).filterIsInstance<Degradation>().single()

private val Gene.transcriptions
    get() = context.get.reactionsOf(this).filterIsInstance<Transcription<*>>()

private val Transcription<*>.regulations
    get() = context.get.reactions.filterIsInstance<Regulation>().filter { it.reaction == this }