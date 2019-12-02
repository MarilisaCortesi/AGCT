package io.github.agct.model.reactions

import io.github.agct.model.entities.BoundEntity
import io.github.agct.model.entities.DegradingEntity
import io.github.agct.model.entities.Entity
import io.github.agct.model.entities.Gene
import io.github.agct.model.entities.MRna
import io.github.agct.model.entities.Protein
import io.github.agct.model.entities.RegulatedGene
import io.github.agct.model.entities.RegulatedMRna
import io.github.agct.model.entities.RegulatingEntity
import io.github.agct.model.entities.TranscribableEntity
import io.github.agct.model.entities.TranscribingEntity
import model.entities.*
import io.github.agct.model.utils.UnsupportedClassException
import io.github.agct.model.utils.checkEquals
import io.github.agct.model.utils.type
import io.github.agct.model.variables.BasicRate
import io.github.agct.model.variables.Rate

internal class BasicSingleReaction(
    override val reagents: Map<Entity, Int> = emptyMap(),
    override val products: Map<Entity, Int> = emptyMap(),
    override val rate: Rate = BasicRate(),
    override val name: String = "reaction"
) : SingleReaction {
    init {
        require(reagents.values.toMutableList().apply { addAll(products.values) }.all { it > 0 }) {
            IllegalArgumentException("All coefficients must be positive.")
        }
    }

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

    private val Map<Entity, Int>.reaction
        get() = entries.joinToString(" + ", "[", "]") {
            if (it.value != 1)
                "${it.value}${it.key.id}"
            else
                it.key.id
        }
}

internal abstract class AbstractGeneticReaction : Reaction {
    override fun toString() =
        buildString {
            append(this@AbstractGeneticReaction.type.toUpperCase())
            append(":\n- ")
            append(reactions.joinToString("\n- "))
        }

    override fun hashCode() =
        reactions.hashCode()

    override fun equals(other: Any?) =
        checkEquals(other) { reactions == it.reactions }
}

internal abstract class AbstractCodingReaction<out C: TranscribingEntity, out T: TranscribableEntity> :
    AbstractGeneticReaction(), CodingReaction<C, T> {
    override val reactions
        get() = setOf(
            BasicSingleReaction(
                reagents = mapOf(coder to 1),
                products = mapOf(coder to 1, target to 1),
                rate = basalRate,
                name = "${target.id} transcription"
            )
        )
}

internal class BasicDegradation(
    override val molecule: DegradingEntity,
    override val degradationRate: Rate = BasicRate()
) : AbstractGeneticReaction(), Degradation {
    override val reactions
        get() = setOf(
            BasicSingleReaction(
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
    override val basalRate: Rate = BasicRate()
) : AbstractCodingReaction<Gene, Protein>(),
    Transcription<Protein>

internal class BasicTranscription(
    override val coder: Gene,
    override val target: MRna,
    override val basalRate: Rate = BasicRate()
) : AbstractCodingReaction<Gene, MRna>(),
    Transcription<MRna>

internal class BasicTranslation(
    override val coder: MRna,
    override val target: Protein,
    override val basalRate: Rate = BasicRate()
) : AbstractCodingReaction<MRna, Protein>(),
    Translation

internal class BasicRegulation(
    override val reaction: CodingReaction<*, *>,
    override val regulator: RegulatingEntity,
    override val regulatingRate: Rate = BasicRate(),
    override val bindingRate: Rate = BasicRate(),
    override val unbindingRate: Rate = BasicRate()
) : AbstractGeneticReaction(), Regulation {
    override val reactions
        get() = regulationInfo.let { (boundEntity, regulatedReaction) ->
            setOf(
                BasicSingleReaction(
                    reagents = mapOf(boundEntity.first to 1, regulator to 1),
                    products = mapOf(boundEntity to 1),
                    rate = bindingRate,
                    name = "${boundEntity.first.id} binding"
                ),
                BasicSingleReaction(
                    reagents = mapOf(boundEntity to 1),
                    products = mapOf(boundEntity.first to 1, regulator to 1),
                    rate = unbindingRate,
                    name = "${boundEntity.first.id} unbinding"
                ),
                *regulatedReaction.reactions.toTypedArray()
            )
        }

    private val regulationInfo : Pair<BoundEntity<*, RegulatingEntity>, CodingReaction<*, *>> =
        when (reaction) {
            is DirectTranscription -> RegulatedGene(
                reaction.coder,
                regulator
            ).let {
                Pair(it, DirectTranscription(it, reaction.target, regulatingRate))
            }
            is BasicTranscription -> RegulatedGene(
                reaction.coder,
                regulator
            ).let {
                Pair(it, BasicTranscription(it, reaction.target, regulatingRate))
            }
            is BasicTranslation -> RegulatedMRna(
                reaction.coder,
                regulator
            ).let {
                Pair(it, BasicTranslation(it, reaction.target, regulatingRate))
            }
            else -> throw UnsupportedClassException(reaction.coder)
        }
}

internal class BasicChemicalReaction(
    override val reagents: Map<Entity, Int> = emptyMap(),
    override val products: Map<Entity, Int> = emptyMap(),
    override val rate: Rate = BasicRate()
) : ChemicalReaction {
    override val reactions: Set<SingleReaction>
        get() = setOf(
            BasicSingleReaction(
                reagents = reagents,
                products = products,
                rate = rate,
                name = "custom chemical reaction"
            )
        )
}