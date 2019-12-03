@file:Suppress("PackageDirectoryMismatch")

package agct

import io.github.agct.model.entities.BasicGene
import io.github.agct.model.entities.BasicMolecule
import io.github.agct.model.entities.BasicProtein
import io.github.agct.model.entities.BasicRegulatingEntity
import io.github.agct.model.entities.DegradingRegulatingEntity
import io.github.agct.model.entities.Entity
import io.github.agct.model.entities.EntityParameters
import io.github.agct.model.entities.Gene
import io.github.agct.model.entities.Protein
import io.github.agct.model.entities.RegulatingEntity
import io.github.agct.model.variables.BasicRate

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

class DslMolecule(override val id: String) : DslDegradable() {
    override val modelEntity: Entity
        get() = BasicMolecule(parameters)
    override var degradationRate: DslRate? = DslRate(default = BasicRate(0.0))
}

private val DslEntity.parameters
    get() = EntityParameters().also {
        it.id = id
        it.initialConcentration = initialConcentration.value
    }