package main.model

import model.entities.*
import model.variables.Concentration
import model.variables.Rate
import model.variables.Variable

private val ALREADY_PRESENT_EXCEPTION = IllegalStateException("Already a molecule with that name.")

class GeneticCircuit {
    private val moleculesMap = mutableMapOf<String, Molecule>()
    private val reactionsMap = mutableMapOf<Gene, MutableMap<Protein, MutableList<Regulator<*>>>>()
    val molecules
        get() = moleculesMap.toMap()

    fun addGene(gene: Gene) {
        insertGene(gene)
    }

    fun addProtein(protein: Protein) {
        insertProtein(protein)
    }

    fun addRegulator(regulator: Regulator<*>) {
        insertRegulator(regulator)
    }

    fun createAgctCircuit() = buildString {
        for ((gene, reaction) in reactionsMap.filter { it.value.isNotEmpty() }) {
            append("Given the ${gene.name}")
            append(reaction.joinToString())
        }

        append("\n")

        for (molecule in moleculesMap.values) {
            append("Taken the ${molecule.name}")
            append(" ")
            append("it has an initial concentration ${molecule.initialConcentration.stringValue}")
            if (molecule is DeterioratingMolecule) {
                append(" ")
                append("and a deterioration rate ${molecule.deteriorationRate.stringValue}")
            }
            append("\n")
        }
    }

    private fun insertMolecule(molecule: Molecule) {
        with(moleculesMap.put(molecule.id, molecule)) {
            if(this != null && this.javaClass != molecule.javaClass) {
                throw ALREADY_PRESENT_EXCEPTION
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

/*
 * UTILITIES
 */

private fun Map<Protein, List<Regulator<*>>>.joinToString() = if (isNotEmpty()) {
    buildString {
        append(" {\n")
        for ((protein, regulators) in this@joinToString) {
            append(1.tabs)
            append("It codes for the ${protein.name} with ${protein.rate}")
            append(regulators.joinToString())
        }
        append("}\n")
    }
} else {
    ""
}

private fun List<Regulator<*>>.joinToString() = if (isNotEmpty()) {
    buildString {
        append(" {\n")
        for (regulator in this@joinToString) {
            append(2.tabs)
            append("Having as regulator the ${regulator.name} with ")
            append(regulator.joinToString())
        }
        append(1.tabs)
        append("}\n")
    }
} else {
    ""
}

private fun Regulator<*>.joinToString() = buildString {
    append(" {\n")
    append(3.tabs)
    append("A unification rate ${unificationRate.stringValue}")
    append("\n")
    append(3.tabs)
    append("A separation rate ${separationRate.stringValue}")
    append("\n")
    append(3.tabs)
    append("A coding rate ${codingRate.stringValue}")
    append("\n")
    append(2.tabs)
    append("}\n")
}

private val Int.tabs
    get() = "\t".repeat(this)

private val Variable<*>.stringValue
    get() = if (values.size == 1) "of ${values.single()}" else "in (${values.joinToString(", ")})"

private val Molecule.name
    get() = when(this) {
        is Gene -> "gene"
        is Protein -> "protein"
        else -> "molecule"
    } + "(\"$id\")"

private val Protein.rate
    get() = "a rate ${codingRate.stringValue}"

private val Regulator<*>.name
    get() = self.name