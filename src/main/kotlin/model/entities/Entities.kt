package model.entities

import model.variables.Concentration

/**
 * An entity inside the genetic circuit, identified by its [id].
 * 
 * The [initial concentration][initialConcentration] is the quantity of molecule present at the beginning.
 */
interface GeneticEntity {
    val id: String
    val aliases: List<String>
    val initialConcentration: Concentration
}

/**
 * An entity that can degrade.
 */
interface DegradingEntity : GeneticEntity

/**
 * An entity that can behave as gene regulator.
 *
 * I.e. a protein can regulate (activating or inhibiting the transcription of another protein by a gene) so it should
 * extend this interface, while a gene should not as it can not behave as a regulator.
 */
interface RegulatingEntity : GeneticEntity

/**
 * An entity that can be a coder in either a transcription or a translation reaction.
 */
interface TranscribingEntity : GeneticEntity

/**
 * An entity that can be a target in either a transcription or a translation reaction.
 */
interface TranscribableEntity : GeneticEntity

/**
 * A gene that codes for an arbitrary number of proteins, passing through the phases of transcription and translation.
 */
interface Gene : GeneticEntity, TranscribingEntity

/**
 * A molecule of mRNA which is the step between the gene and the protein that it codes for.
 */
interface MRna : DegradingEntity, TranscribingEntity, TranscribableEntity

/**
 * A protein which can be coded by a gene.
 */
interface Protein : DegradingEntity, RegulatingEntity, TranscribableEntity

/**
 * A [genetic entity][GeneticEntity] composed of two bound single entities.
 */
interface BoundEntity<out F : GeneticEntity, out S : GeneticEntity> : GeneticEntity {
    val first: F
    val second: S
}