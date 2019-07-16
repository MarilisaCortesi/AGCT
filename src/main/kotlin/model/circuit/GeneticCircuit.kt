package model.circuit

import model.entities.Gene
import model.entities.Molecule
import model.entities.Protein
import model.entities.Regulator

interface GeneticCircuit {
    /**
     * The circuit's name.
     */
    val name: String

    /**
     * A map containing all the molecules of the circuit, indexed by their ids.
     */
    val molecules: Map<String, Molecule>

    /**
     * A map containing the relationships between the molecules.
     */
    val reactions: Map<Gene, MutableMap<Protein, MutableList<Regulator<*>>>>

    /**
     * Adds a gene to the circuit.
     *
     * @param gene the gene to be added.
     */
    fun addGene(gene: Gene)


    /**
     * Adds a protein to the circuit.
     *
     * @param protein the protein to be added.
     */
    fun addProtein(protein: Protein)


    /**
     * Adds a regulator to the circuit.
     *
     * @param regulator the regulator to be added.
     */
    fun addRegulator(regulator: Regulator<*>)
}