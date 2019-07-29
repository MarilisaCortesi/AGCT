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
    val molecule: DegradingMolecule
    val degradationRate: Rate
}

/**
 * The transcription (and translation) of a [gene][coder] into a [protein][target] passing through a molecule of [mRNA][step].
 */
internal interface Transcription : BiochemicalReaction {
    val coder: Gene
    val step: MRna?
    val target: Protein
    val transcriptionRate: Rate
    val translationRate: Rate?
}

/**
* The regulation of a [transcription][reaction] from a [regulator].
* The translation, if present, is not affected by the regulation.
*
* The [binding rate][bindingRate] is the rate at which the regulator links itself to the gene.
* The [unbinding rate][unbindingRate] is the rate at which the regulated gene separates in the two entities.
*/
internal interface Regulation : BiochemicalReaction {
    val regulator: RegulatingMolecule
    val reaction: Transcription
    val regulatedRate: Rate
    val bindingRate: Rate
    val unbindingRate: Rate
}