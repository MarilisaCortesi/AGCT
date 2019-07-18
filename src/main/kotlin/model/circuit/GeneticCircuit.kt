package model.circuit

import model.entities.Gene
import model.entities.Molecule
import model.entities.Protein
import model.entities.Regulator

/**
 * Represents a [genetic circuit][GeneticCircuit] with a [name], its [molecules] and the [reactions] between them.
 */
interface GeneticCircuit {
    val name: String

    val molecules: Map<String, Molecule>

    val reactions: Map<Gene, MutableMap<Protein, MutableList<Regulator<*>>>>

    /**
     * Adds a [gene] to the circuit.
     */
    fun addGene(gene: Gene)


    /**
     * Adds a [protein] to the circuit.
     */
    fun addProtein(protein: Protein)


    /**
     * Adds a [regulator] to the circuit.
     */
    fun addRegulator(regulator: Regulator<*>)
}