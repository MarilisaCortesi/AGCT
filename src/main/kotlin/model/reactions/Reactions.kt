package model.reactions

import model.entities.*
import model.variables.Rate

/**
 * A biochemicalReaction that occurs with a given [rate].
 * The [reagents] and the [products] of the biochemicalReaction contain their coefficients.
 */
internal interface Reaction {
    val reagents: Map<BiochemicalEntity, Int>
    val products: Map<BiochemicalEntity, Int>
    val rate: Rate
    val name: String
}

/**
 * A biochemical biochemicalReaction that can be made of multiple [single reactions][reactions].
 */
internal interface BiochemicalReaction {
    val reactions: Set<Reaction>
}

/**
 * The degradation of a [molecule].
 */
internal interface Degradation : BiochemicalReaction {
    val molecule: DegradingEntity
    val degradationRate: Rate
}

/**
 * The coding of a [target] from a [coder].
 */
internal interface CodingReaction<out C : TranscribingEntity, out T : TranscribableEntity> : BiochemicalReaction {
    val coder: C
    val target: T
    val basalRate: Rate
}

/**
 * The transcription of a [gene][coder] into an [molecule][target] that can either be a [Protein] or [mRNA][MRna].
 */
internal interface Transcription<out T : TranscribableEntity> : CodingReaction<Gene, T>

/**
 * The translation of a [protein][target] from a molecule of [mRNA][coder].
 */
internal interface Translation : CodingReaction<MRna, Protein>

/**
* The regulation of a [coding reaction][reaction] from a [regulator].
*
* The [binding rate][bindingRate] is the rate at which the regulator links itself to the entity.
* The [unbinding rate][unbindingRate] is the rate at which the bound entity separates in the two original entities.
*/
internal interface Regulation : BiochemicalReaction {
    val reaction: CodingReaction<*, *>
    val regulator: RegulatingEntity
    val regulatingRate: Rate
    val bindingRate: Rate
    val unbindingRate: Rate
}