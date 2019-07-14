package model.circuit

import main.model.GeneticCircuit
import model.entities.*

class BasicGeneticCircuit(override val name: String) : GeneticCircuit {
    private val moleculesMap = mutableMapOf<String, Molecule>()
    private val reactionsMap = mutableMapOf<Gene, MutableMap<Protein, MutableList<Regulator<*>>>>()

    override val molecules
        get() = moleculesMap.toMap()

    override val reactions
        get() = reactionsMap.toMap()

    override fun addGene(gene: Gene) {
        insertGene(gene)
    }

    override fun addProtein(protein: Protein) {
        insertProtein(protein)
    }

    override fun addRegulator(regulator: Regulator<*>) {
        insertRegulator(regulator)
    }

    private fun insertMolecule(molecule: Molecule) {
        with(moleculesMap.put(molecule.id, molecule)) {
            if(this != null && this.javaClass != molecule.javaClass) {
                throw IllegalStateException("Already a molecule with that name.")
            }
        }
    }

    private fun insertGene(gene: Gene) = with(gene) {
        insertMolecule(this)
        reactionsMap.getOrPut(this) { mutableMapOf() }
    }

    private fun insertProtein(protein: Protein) = with(protein) {
        insertMolecule(this)
        insertGene(coder).getOrPut(this) { mutableListOf() }
    }

    private fun insertRegulator(regulator: Regulator<*>) = with(regulator) {
        moleculesMap.putIfAbsent(self.id, self)
        insertProtein(target).add(this)
    }
}