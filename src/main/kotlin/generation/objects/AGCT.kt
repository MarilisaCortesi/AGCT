@file:Suppress("PackageDirectoryMismatch")

package agct

import generation.AbstractGenerator
import generation.defaultDirectory
import generation.utils.Level
import generation.utils.Level.Companion.start
import model.circuit.GeneticCircuit
import model.entities.*
import model.reactions.ChemicalReaction
import model.reactions.Degradation
import model.reactions.Regulation
import model.reactions.Transcription
import model.utils.UnsupportedClassException
import model.utils.string
import model.variables.Variable

object AGCT : AGCTGenerator({ "$defaultDirectory/agct.kt" })

open class AGCTGenerator(filename: GeneticCircuit.() -> String) : AbstractGenerator({ file ->
    context = this

    file[filename()] =
        start(prefix = " {", postfix = "}") { // Level
            "import agct.*"()
            line()
            "fun main()" { // Level
                "Create circuit ${name.string} containing" { // Level
                    genes.forEach { gene ->
                        gene.transcriptions(line = "the ${gene.string} that codes", innerLine = " and", spacings = -1) { transcription ->
                            "the ${transcription.target.string}"()
                            "with a basal.rate ${transcription.basalRate.string}"()
                            transcription.regulations(line = "regulated by", innerLine = " and", spacings = -1) { regulation ->
                                "the ${regulation.regulator.string}"()
                                "with a regulating.rate ${regulation.regulatingRate.string}"()
                                "with a binding.rate ${regulation.bindingRate.string}"()
                                "with an unbinding.rate ${regulation.unbindingRate.string}"()
                            }
                        }
                        line()
                    }
                    chemicalReactions { reaction ->
                        reaction.line()
                    }
                    line()
                    agctEntities(line = { "\"${it.id}\"" }, spacings = 1) { entity ->
                        "has an initial.concentration ${entity.initialConcentration.string}"()
                        if (entity is DegradingEntity) {
                            "has a degradation.rate ${entity.degradation.degradationRate.string}"()
                        }
                    }
                }
            }
        }.toString()

    context = null
})

private var context: GeneticCircuit? = null

private val GeneticCircuit.genes
    get() = entities.filterIsInstance<Gene>().filter { it !is RegulatedGene }

private val GeneticCircuit.agctEntities
    get() = entities.filter { it !is BoundEntity<*, *> }

private val DegradingEntity.degradation
    get() = context!!.reactionsOf(this).filterIsInstance<Degradation>().single()

private val Gene.transcriptions
    get() = context!!.reactionsOf(this).filterIsInstance<Transcription<*>>()

private val Transcription<*>.regulations
    get() = context!!.reactions.filterIsInstance<Regulation>().filter { it.reaction == this }

private fun Level.chemicalReactions(block: Level.(ChemicalReaction) -> Unit) =
    context!!.reactions.filterIsInstance<ChemicalReaction>().let { reactions ->
        if (reactions.isNotEmpty()) {
            "chemical reactions" {
                reactions.forEach { block(it) }
            }
        }
    }

private val ChemicalReaction.line
    get() = reactions.single()
        .toString()
        .substringAfter(":")
        .substringBefore(",")
        .replace("[", "\"")
        .replace("]", "\"")
        .replace("-->", "to")
        .trim() + " having reaction.rate ${rate.string}"

private val Entity.string
    get() = when (this) {
        is Gene -> "gene"
        is Protein -> "protein"
        is RegulatingEntity -> "molecule"
        else -> throw UnsupportedClassException(this)
    } + " ${id.string}"

private val Variable<*>.string
    get() = if (values.size == 1) "of ${values.single()}"
            else "into values${values.joinToString(", ", "(", ")")}"
