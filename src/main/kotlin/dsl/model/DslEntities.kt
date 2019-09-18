@file:Suppress("PackageDirectoryMismatch")

package agct

import model.entities.*

abstract class DslEntity internal constructor() {
    internal abstract val modelEntity: Entity

    internal abstract val id: String

    internal val initialConcentration = TopLevel.circuit.default.initialConcentration
}

abstract class DslDegradable internal constructor() : DslEntity() {
    internal abstract var degradationRate: DslRate?
}

abstract class DslRegulating internal constructor() : DslDegradable() {
    abstract override val modelEntity: RegulatingEntity
}

class DslGene(override val id: String) : DslEntity() {
    override val modelEntity: Gene
        get() = BasicGene(parameters)
}

class DslProtein(override val id: String) : DslRegulating() {
    override val modelEntity: Protein
        get() = BasicProtein(parameters)

    override var degradationRate: DslRate? = TopLevel.circuit.default.degradationRate
}

class DslRegulator(override val id: String) : DslRegulating() {
    override val modelEntity: RegulatingEntity
        get() = if(degradationRate == null) {
            BasicRegulatingEntity(parameters)
        } else {
            DegradingRegulatingEntity(parameters)
        }

    override var degradationRate: DslRate? = null
}

class DslMolecule(override val id: String) : DslEntity() {
    override val modelEntity: Entity
        get() = BasicMolecule(parameters)
}

private val DslEntity.parameters
    get() = EntityParameters().also {
        it.id = id
        it.initialConcentration = initialConcentration.value
    }