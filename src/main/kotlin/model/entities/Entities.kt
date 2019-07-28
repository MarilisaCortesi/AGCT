package model.entities

import model.variables.Concentration

/**
 * An entity inside the genetic circuit, identified by its [id].
 * 
 * The [initial concentration][initialConcentration] is the quantity of molecule present at the beginning.
 */
internal interface BiochemicalEntity {
    val id: String
    val aliases: List<String>
    val initialConcentration: Concentration
}

/**
 * A generic molecule.
 */
internal interface Molecule : BiochemicalEntity

/**
 * A molecule that can degrade.
 */
internal interface DegradingMolecule : BiochemicalEntity

/**
 * A molecule that can be a gene regulator.
 *
 * I.e. a protein can regulate (activating or inhibiting the transcription of another protein by a gene) so it should
 * extend this interface, while a gene should not as it can not behave as a regulator.
 */
internal interface RegulatingMolecule : BiochemicalEntity

/**
 * A gene that codes for an arbitrary number of proteins, passing through the phases of transcription and translation.
 */
internal interface Gene : BiochemicalEntity

/**
 * A molecule of mRNA which is the step between the gene and the protein that it codes for.
 */
internal interface MRna : DegradingMolecule

/**
 * A protein which can be coded by a gene.
 */
internal interface Protein : DegradingMolecule, RegulatingMolecule

/**
 * A [biochemical entity][BiochemicalEntity] composed of two bound single entities.
 */
internal interface BoundBiochemicalEntity<out F : BiochemicalEntity, out S : BiochemicalEntity> : BiochemicalEntity {
    val first: F
    val second: S
}