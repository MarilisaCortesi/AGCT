package dsl.model

import model.entities.*
import model.entities.BasicGene
import model.entities.BiochemicalEntity
import model.entities.EntityParameters
import model.entities.Gene
import model.entities.Protein
import model.variables.Concentration

abstract class DslEntity internal constructor(internal val id: String) {
    internal abstract val biochemicalEntity: BiochemicalEntity

    internal open val parameters
        get() = EntityParameters().also {
            it.id = id
            it.initialConcentration = initialConcentration.concentration ?: Concentration()
        }

    val initialConcentration = DslConcentration()
}

abstract class DslDegradingRegulating internal constructor(id: String) : DslEntity(id) {
    abstract override val biochemicalEntity: RegulatingMolecule

    val degradationRate = DslRate()
}

class DslGene(id: String) : DslEntity(id) {
    override val biochemicalEntity: Gene
        get() = BasicGene(parameters)
}

class DslProtein(id: String) : DslDegradingRegulating(id) {
    override val biochemicalEntity: Protein
        get() = BasicProtein(parameters)
}

class DslRegulator(id: String) : DslDegradingRegulating(id) {
    override val biochemicalEntity: RegulatingMolecule
        get() = if(degradationRate.rate == null) {
            BasicRegulatingMolecule(parameters)
        } else {
            BasicDegradingRegulatingMolecule(parameters)
        }
}