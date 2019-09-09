package model.reactions

import model.entities.*
import model.variables.Rate

/**
 * A reaction that occurs with a given [rate].
 * The [reagents] and the [products] of the reaction contain their coefficients.
 */
interface Reaction {
    val reagents: Map<GeneticEntity, Int>
    val products: Map<GeneticEntity, Int>
    val rate: Rate
    val name: String
}

/**
 * A genetic reaction that can be made of multiple [single reactions][reactions].
 */
interface GeneticReaction {
    val reactions: Set<Reaction>
}

/**
 * The degradation of a [molecule].
 */
interface Degradation : GeneticReaction {
    val molecule: DegradingEntity
    val degradationRate: Rate
}

/**
 * The coding of a [target] from a [coder].
 */
interface CodingReaction<out C : TranscribingEntity, out T : TranscribableEntity> : GeneticReaction {
    val coder: C
    val target: T
    val basalRate: Rate
}

/**
 * The transcription of a [gene][coder] into an [molecule][target] that can either be a [Protein] or [mRNA][MRna].
 */
interface Transcription<out T : TranscribableEntity> : CodingReaction<Gene, T>

/**
 * The translation of a [protein][target] from a molecule of [mRNA][coder].
 */
interface Translation : CodingReaction<MRna, Protein>

/**
* The regulation of a [coding reaction][reaction] from a [regulator].
*
* The [binding rate][bindingRate] is the rate at which the regulator links itself to the entity.
* The [unbinding rate][unbindingRate] is the rate at which the bound entity separates in the two original entities.
*/
interface Regulation : GeneticReaction {
    val reaction: CodingReaction<*, *>
    val regulator: RegulatingEntity
    val regulatingRate: Rate
    val bindingRate: Rate
    val unbindingRate: Rate
}