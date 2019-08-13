package model.reactions

import model.utils.checkEquals
import model.utils.type
import model.entities.*
import model.utils.UnsupportedClassException
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
            append(", rate = ")
            append(rate.values)
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
            append(this@AbstractBiochemicalReaction.type.toUpperCase())
            append(":\n- ")
            append(reactions.joinToString("\n- "))
        }

    override fun hashCode() =
        reactions.hashCode()

    override fun equals(other: Any?) =
        checkEquals(other) { reactions == it.reactions }
}

internal abstract class AbstractCodingReaction<out C: TranscribingEntity, out T: TranscribableEntity> :
    AbstractBiochemicalReaction(), CodingReaction<C, T> {
    override val reactions
        get() = setOf(
            BasicReaction(
                reagents = mapOf(coder to 1),
                products = mapOf(coder to 1, target to 1),
                rate = basalRate,
                name = "${target.id} transcription"
            )
        )
}

internal class BasicReaction(
    reagents: Map<BiochemicalEntity, Int> = emptyMap(),
    products: Map<BiochemicalEntity, Int> = emptyMap(),
    rate: Rate = Rate(),
    name: String = "biochemicalReaction"
) : AbstractReaction(reagents, products, rate, name)

internal class BasicDegradation(
    override val molecule: DegradingEntity,
    override val degradationRate: Rate = Rate()
) : AbstractBiochemicalReaction(), Degradation {
    override val reactions
        get() = setOf(
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
    override val basalRate: Rate = Rate()
) : AbstractCodingReaction<Gene, Protein>(), Transcription<Protein>

internal class BasicTranscription(
    override val coder: Gene,
    override val target: MRna,
    override val basalRate: Rate = Rate()
) : AbstractCodingReaction<Gene, MRna>(), Transcription<MRna>

internal class BasicTranslation(
    override val coder: MRna,
    override val target: Protein,
    override val basalRate: Rate = Rate()
) : AbstractCodingReaction<MRna, Protein>(), Translation

internal class BasicRegulation(
    override val reaction: CodingReaction<*, *>,
    override val regulator: RegulatingEntity,
    override val regulatedRate: Rate = Rate(),
    override val bindingRate: Rate = Rate(),
    override val unbindingRate: Rate = Rate()
) : AbstractBiochemicalReaction(), Regulation {
    override val reactions
        get() = regulationInfo.let { (boundEntity, regulatedReaction) ->
            setOf(
                BasicReaction(
                    reagents = mapOf(boundEntity.first to 1, regulator to 1),
                    products = mapOf(boundEntity to 1),
                    rate = bindingRate,
                    name = "${boundEntity.first.id} binding"
                ),
                BasicReaction(
                    reagents = mapOf(boundEntity to 1),
                    products = mapOf(boundEntity.first to 1, regulator to 1),
                    rate = unbindingRate,
                    name = "${boundEntity.first.id} unbinding"
                ),
                *regulatedReaction.reactions.toTypedArray()
            )
        }

    private val regulationInfo : Pair<BoundBiochemicalEntity<*, RegulatingEntity>, CodingReaction<*, *>> =
        when (reaction) {
            is DirectTranscription -> RegulatedGene(reaction.coder, regulator).let {
                Pair(it, DirectTranscription(it, reaction.target, regulatedRate))
            }
            is BasicTranscription -> RegulatedGene(reaction.coder, regulator).let {
                Pair(it, BasicTranscription(it, reaction.target, regulatedRate))
            }
            is BasicTranslation -> RegulatedMRna(reaction.coder, regulator).let {
                Pair(it, BasicTranslation(it, reaction.target, regulatedRate))
            }
            else -> throw UnsupportedClassException(reaction.coder)
        }
}