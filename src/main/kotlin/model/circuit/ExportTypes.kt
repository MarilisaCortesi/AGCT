package model.circuit

internal enum class ExportTypes(val from: GeneticCircuit.() -> Unit) {
    AGCT({
        throw NotImplementedError("")
    }),
    Alchemist({
        throw NotImplementedError("")
    });
}

/*
    append("CIRCUIT \"${name.toUpperCase()}\"\n\n\n")

    for ((gene, biochemicalReaction) in reactions.filter { it.value.isNotEmpty() }) {
        append("Given the ${gene.name}")
        append(biochemicalReaction.joinToString())
    }

    append("\n")

    for (molecule in entities.values) {
        append("Taken the ${molecule.name}")
        append(" ")
        append("it has an initial concentration ${molecule.initialConcentration.stringValue}")
        if (molecule is DeterioratingMolecule) {
            append(" ")
            append("and a deterioration rate ${molecule.deteriorationRate.stringValue}")
        }
        append("\n")
    }
}.let { println(it) }

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
    append("A unification rate ${bindingRate.stringValue}")
    append("\n")
    append(3.tabs)
    append("A separation rate ${unbindingRate.stringValue}")
    append("\n")
    append(3.tabs)
    append("A coding rate ${codingRate.stringValue}")
    append("\n")
    append(2.tabs)
    append("}\n")
}
    */

/*
private val Int.tabs
    get() = "\t".repeat(this)

private val Variable<*>.stringValue
    get() = if (values.size == 1) "of ${values.single()}" else "in (${values.joinToString(", ")})"

private val BiochemicalEntity.name
    get() = when(this) {
        is Gene -> "gene"
        is Protein -> "protein"
        else -> "molecule"
    } + "(\"$id\")"

private val Protein.rate
    get() = "a rate ${basalRate.stringValue}"

private val Regulator<*>.name
    get() = self.name
*/