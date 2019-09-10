@file:Suppress("PackageDirectoryMismatch")

package agct

import model.entities.*
import model.entities.BasicGene
import model.entities.GeneticEntity
import model.entities.EntityParameters
import model.entities.Gene
import model.entities.Protein

abstract class DslEntity internal constructor() {
    internal abstract val geneticEntity: GeneticEntity

    internal abstract val id: String

    internal val initialConcentration = TopLevel.circuit.default.initialConcentration
}

abstract class DslDegradable internal constructor() : DslEntity() {
    internal abstract var degradationRate: DslRate?
}

abstract class DslRegulating internal constructor() : DslDegradable() {
    abstract override val geneticEntity: RegulatingEntity
}

class DslGene(override val id: String) : DslEntity() {
    override val geneticEntity: Gene
        get() = BasicGene(parameters)
}

class DslProtein(override val id: String) : DslRegulating() {
    override val geneticEntity: Protein
        get() = BasicProtein(parameters)

    override var degradationRate: DslRate? = TopLevel.circuit.default.degradationRate
}

class DslMolecule(override val id: String) : DslRegulating() {
    override val geneticEntity: RegulatingEntity
        get() = if(degradationRate == null) {
            BasicRegulatingEntity(parameters)
        } else {
            DegradingRegulatingEntity(parameters)
        }

    override var degradationRate: DslRate? = null
}

private val DslEntity.parameters
    get() = EntityParameters().also {
        it.id = id
        it.initialConcentration = initialConcentration.value
    }