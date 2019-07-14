package model.entities

import model.variables.Concentration
import model.variables.Rate

/**
 * A molecule inside the genetic circuitContext.
 *
 * @property id the identificator of the molecule, which will be used when creating the reactions
 * @property fullName the full name of the molecule, which will be used in the verbose circuitContext description
 * @property initialConcentration the initial initialConcentration of the molecule
 */
interface Molecule {
    val id: String
    var fullName: String
    var initialConcentration: Concentration
}

/**
 * A gene, which codes for an arbitrary number of proteins.
 */
interface Gene : Molecule

/**
 * A molecule that can be a gene regulator.
 *
 * I.e. a protein can regulate (activating or inhibiting the transcription of another protein by a gene) so it should
 * extend this interface, while a gene should not as it can not behave as a regulator.
 */
interface RegulativeMolecule : Molecule

/**
 * A molecule with a deterioration rate.
 *
 * @property deteriorationRate the rate at which the molecule deteriorates
 */
interface DeterioratingMolecule : Molecule {
    var deteriorationRate: Rate
}

/**
 * A protein, which can be coded by one gene only.
 *
 * @property coder the gene which codes for the protein
 * @property basalRate the rate at which the transcription occurs
 */
interface Protein : DeterioratingMolecule, RegulativeMolecule {
    val coder: Gene
    var basalRate: Rate
}

/**
 * A molecule which can regulate the rate at which a transcription occurs.
 *
 * @param R the type of the regulator
 * @property self the regulator molecule
 * @property target the protein (and, subsequently, the gene) which will be affected by this regulation
 * @property unificationRate the rate at which the regulator links itself to the gene
 * @property separationRate the rate at which the regulated gene separates in regulator and original gene
 * @property codingRate the rate at which the regulated gene codes for the protein
 */
interface Regulator<out R : RegulativeMolecule> : Molecule {
    val self: R
    val target: Protein
    var unificationRate: Rate
    var separationRate: Rate
    var codingRate: Rate
}