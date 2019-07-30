package model.reactions

import model.utils.UnknownClassException
import model.utils.checkEquals
import model.utils.className
import model.entities.*
import model.variables.Rate

internal abstract class AbstractReaction(
    override val reagents: Map<BiochemicalEntity, Int>,
    override val products: Map<BiochemicalEntity, Int>,
    override val rate: Rate,
    override val name: String
) : Reaction {
    override fun toString() =
        buildString {
            append(name)
            append(": ")
            append(reagents.reaction)
            append(" --> ")
            append(products.reaction)
        }

    override fun hashCode() =
        (reagents to products).hashCode()

    override fun equals(other: Any?) =
        checkEquals(other) { reagents == it.reagents && products == it.products }

    private val Map<BiochemicalEntity, Int>.reaction
        get() = if (size == 0) {
            "[]"
        } else {
            entries.joinToString(" + ") { if (it.value != 1) "${it.value} " else "" + it.key.id }
        }
}

internal abstract class AbstractBiochemicalReaction : BiochemicalReaction {
    override fun toString() =
        buildString {
            append(className.toUpperCase())
            append(":\n- ")
            append(reactions.joinToString("\n- "))
        }

    override fun hashCode() =
        reactions.hashCode()

    override fun equals(other: Any?) =
        checkEquals(other) { reactions == it.reactions }
}

internal class BasicReaction(
    reagents: Map<BiochemicalEntity, Int> = emptyMap(),
    products: Map<BiochemicalEntity, Int> = emptyMap(),
    rate: Rate = Rate(),
    name: String = "biochemicalReaction"
) : AbstractReaction(reagents, products, rate, name)

internal class BasicDegradation(
    override val molecule: DegradingMolecule,
    override val degradationRate: Rate = Rate()
) : AbstractBiochemicalReaction(), Degradation {
    override val reactions =
        setOf(
            BasicReaction(
                reagents = mapOf(molecule to 1),
                products = emptyMap(),
                rate = degradationRate,
                name = "${molecule.id} degradation"
            )
        )
}

internal class DirectTranscription(
    override val coder: Gene,
    override val target: Protein,
    override val transcriptionRate: Rate = Rate()
) : AbstractBiochemicalReaction(), Transcription {
    override val step: MRna? = null
    override val translationRate: Rate? = null
    override val reactions =
        setOf(
            BasicReaction(
                reagents = mapOf(coder to 1),
                products = mapOf(coder to 1, target to 1),
                rate = transcriptionRate,
                name = "${target.id} transcription"
            )
        )
}

internal class TwoStepTranscription(
    override val coder: Gene,
    override val step: MRna,
    override val target: Protein,
    override val transcriptionRate: Rate = Rate(),
    override val translationRate: Rate = Rate()
) : AbstractBiochemicalReaction(), Transcription {
    override val reactions: Set<Reaction> =
        setOf(
            BasicReaction(
                reagents = mapOf(coder to 1),
                products = mapOf(coder to 1, step to 1),
                rate = transcriptionRate,
                name = "${step.id} transcription"
            ),
            BasicReaction(
                reagents = mapOf(step to 1),
                products = mapOf(step to 1, target to 1),
                rate = translationRate,
                name = "${target.id} translation"
            )
        )
}

internal class BasicRegulation(
    override val reaction: Transcription,
    override val regulator: RegulatingMolecule,
    override val regulatedRate: Rate = Rate(),
    override val bindingRate: Rate = Rate(),
    override val unbindingRate: Rate = Rate()
) : AbstractBiochemicalReaction(), Regulation {
    override val reactions =
        RegulatedGene(reaction.coder, regulator).let { bound ->
            setOf(
                BasicReaction(
                    reagents = mapOf(reaction.coder to 1, regulator to 1),
                    products = mapOf(bound to 1),
                    rate = bindingRate,
                    name = "${reaction.coder.id} binding"
                ),
                BasicReaction(
                    reagents = mapOf(bound to 1),
                    products = mapOf(reaction.coder to 1, regulator to 1),
                    rate = unbindingRate,
                    name = "${bound.id} unbinding"
                ),
                *with(reaction) {
                    when(this) {
                        is DirectTranscription -> DirectTranscription(bound, target, transcriptionRate)
                        is TwoStepTranscription -> TwoStepTranscription(bound, step, target, transcriptionRate, translationRate)
                        else -> throw UnknownClassException()
                    }.reactions.toTypedArray()
                }
            )
        }
    }