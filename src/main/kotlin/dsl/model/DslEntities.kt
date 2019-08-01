package dsl.model

import dsl.levels.TopLevel.Circuit.default
import model.entities.*
import model.entities.BasicGene
import model.entities.BiochemicalEntity
import model.entities.EntityParameters
import model.entities.Gene
import model.entities.Protein

abstract class DslEntity internal constructor(internal val id: String) {
    internal abstract val biochemicalEntity: BiochemicalEntity

    internal val initialConcentration = default.initialConcentration.copy
}

abstract class DslDegradable internal constructor(id: String) : DslEntity(id) {
    internal abstract var degradation: DslDegradation?
}

abstract class DslRegulating internal constructor(id: String) : DslDegradable(id) {
    abstract override val biochemicalEntity: RegulatingEntity
}

class DslGene(id: String) : DslEntity(id) {
    override val biochemicalEntity: Gene
        get() = BasicGene(parameters)
}

class DslProtein(id: String) : DslRegulating(id) {
    override val biochemicalEntity: Protein
        get() = BasicProtein(parameters)

    override var degradation: DslDegradation? = DslDegradation().also { it.entity = this }
}

class DslMolecule(id: String) : DslRegulating(id) {
    override val biochemicalEntity: RegulatingEntity
        get() = if(degradation == null) {
            BasicRegulatingEntity(parameters)
        } else {
            DegradingRegulatingMolecule(parameters)
        }

    override var degradation: DslDegradation? = null
}

private val DslEntity.parameters
    get() = EntityParameters().also {
        it.id = id
        it.initialConcentration = initialConcentration.concentration
    }