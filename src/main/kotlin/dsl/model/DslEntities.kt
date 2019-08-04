@file:Suppress("PackageDirectoryMismatch")

package dsl

import model.entities.*
import model.entities.BasicGene
import model.entities.BiochemicalEntity
import model.entities.EntityParameters
import model.entities.Gene
import model.entities.Protein

abstract class DslEntity internal constructor(default: DefaultValues) {
    internal abstract val biochemicalEntity: BiochemicalEntity

    internal abstract val id: String

    internal val initialConcentration = default.initialConcentration
}

abstract class DslDegradable internal constructor(default: DefaultValues) : DslEntity(default) {
    internal abstract var degradationRate: DslRate?
}

abstract class DslRegulating internal constructor(default: DefaultValues) : DslDegradable(default) {
    abstract override val biochemicalEntity: RegulatingEntity
}

class DslGene(default: DefaultValues, override val id: String) : DslEntity(default) {
    override val biochemicalEntity: Gene
        get() = BasicGene(parameters)
}

class DslProtein(default: DefaultValues, override val id: String) : DslRegulating(default) {
    override val biochemicalEntity: Protein
        get() = BasicProtein(parameters)

    override var degradationRate: DslRate? = default.degradationRate
}

class DslMolecule(default: DefaultValues, override val id: String) : DslRegulating(default) {
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