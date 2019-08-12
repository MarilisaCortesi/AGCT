@file:Suppress("PackageDirectoryMismatch")

package dsl

import model.entities.*
import model.entities.BasicGene
import model.entities.BiochemicalEntity
import model.entities.EntityParameters
import model.entities.Gene
import model.entities.Protein

abstract class DslEntity internal constructor() {
    internal abstract val biochemicalEntity: BiochemicalEntity

    internal abstract val id: String

    internal val initialConcentration = TopLevel.circuit.default.initialConcentration
}

abstract class DslDegradable internal constructor() : DslEntity() {
    internal abstract var degradationRate: DslRate?
}

abstract class DslRegulating internal constructor() : DslDegradable() {
    abstract override val biochemicalEntity: RegulatingEntity
}

class DslGene(override val id: String) : DslEntity() {
    override val biochemicalEntity: Gene
        get() = BasicGene(parameters)
}

class DslProtein(override val id: String) : DslRegulating() {
    override val biochemicalEntity: Protein
        get() = BasicProtein(parameters)

    override var degradationRate: DslRate? = TopLevel.circuit.default.degradationRate
}

class DslMolecule(override val id: String) : DslRegulating() {
    override val biochemicalEntity: RegulatingEntity
        get() = if(degradationRate == null) {
            BasicRegulatingEntity(parameters)
        } else {
            DegradingRegulatingMolecule(parameters)
        }

    override var degradationRate: DslRate? = null
}

private val DslEntity.parameters
    get() = EntityParameters().also {
        it.id = id
        it.initialConcentration = initialConcentration.value
    }