package model.entities

import model.variables.Concentration
import model.variables.Rate

/**
 * A molecule inside the genetic circuit, identified by its [id].
 */
interface Molecule {
    val id: String
    var fullName: String
    var initialConcentration: Concentration
}

/**
 * A gene that codes for an arbitrary number of proteins.
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
 * A molecule with a [deterioration rate][deteriorationRate].
 */
interface DeterioratingMolecule : Molecule {
    var deteriorationRate: Rate
}

/**
 * A protein, which can be coded by one [gene][coder] only with a given [basal rate][basalRate].
 */
interface Protein : DeterioratingMolecule, RegulativeMolecule {
    val coder: Gene
    var basalRate: Rate
}

/**
 * A [molecule][self] of type [R] which can regulate the rate at which the transcription of a [protein][target] occurs.
 *
 * The [unification rate][unificationRate] is the rate at which the regulator links itself to the gene.
 * The [separation rate][separationRate] is the rate at which the regulated gene separates in the two molecules.
 * The [coding rate][codingRate] is the rate at which the regulated gene codes for the protein.
 */
interface Regulator<out R : RegulativeMolecule> : Molecule {
    val self: R
    val target: Protein
    var unificationRate: Rate
    var separationRate: Rate
    var codingRate: Rate
}