package model.circuit.export

import model.circuit.GeneticCircuit
import model.entities.*
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
import model.utils.UnsupportedClassException
import model.utils.className
import model.utils.toConsole
import model.variables.Variable

internal fun GeneticCircuit.agctExport() = start(" {", "}") {
    block("Create circuit \"$name\" containing") {
        genes.blocks({ "the gene \"$id\" that" }) {
            transcriptions.blocks({ "codes for" }) {
                line("the protein \"${target.id}\"")
                line("with a basalRate ${basalRate.string}")
                regulations.blocks({ "regulated by" }) {
                    line("the regulator \"${regulator.id}\"")
                    line("with a regulatedRate ${regulatedRate.string}")
                    line("with a bindingRate ${bindingRate.string}")
                    line("with an unbindingRate ${unbindingRate.string}")
                }
            }
        }
        line()
        dslEntities.blocks({ string }) {
            line("has an initialConcentration ${initialConcentration.string}")
            if (this is DegradingEntity) {
                line("has a degradationRate ${degradation.degradationRate.string}")
            }
        }
    }
}.toConsole()

private val GeneticCircuit.genes
    get() = entities.filterIsInstance<Gene>().filter { it !is RegulatedGene }

private val GeneticCircuit.dslEntities
    get() = entities.filter { it !is BoundBiochemicalEntity<*, *> }

private val BiochemicalEntity.string
    get() = when (this) {
        is Gene -> "gene"
        is Protein -> "protein"
        is RegulatingEntity -> "regulator"
        else -> throw UnsupportedClassException("$this has an unsupported class type")
    } + "(\"$id\")"

private val DegradingEntity.degradation
    get() = Context.context.circuit.reactionsOf(this).filterIsInstance<Degradation>().single()

private val Gene.transcriptions
    get() = Context.context.circuit.reactionsOf(this).filterIsInstance<Transcription<*>>()

private val Transcription<*>.regulations
    get() = Context.context.circuit.reactions.filterIsInstance<Regulation>().filter { it.reaction == this }

private val Variable<*>.string
    get() = if (values.size == 1) "of ${values.single()}" else "into values(${values.joinToString(", ")})"