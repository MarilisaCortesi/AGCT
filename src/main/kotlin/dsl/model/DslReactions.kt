@file:Suppress("PackageDirectoryMismatch")

package agct

import model.reactions.*
import model.reactions.BasicRegulation
import model.reactions.DirectTranscription

abstract class DslReaction internal constructor() {
    internal abstract val modelReaction: Reaction
}

class DslTranscription internal constructor(
    private val coder: DslGene,
    private val target: DslProtein
) : DslReaction() {
    internal var basalRate: DslRate = TopLevel.circuit.default.basalRate

    override val modelReaction: DirectTranscription
        get() = DirectTranscription(
            coder.modelEntity,
            target.modelEntity,
            basalRate.value
        )
}

class DslRegulation internal constructor(
    private val transcription: DslTranscription,
    private val regulator: DslRegulating
) : DslReaction() {
    internal var regulatingRate: DslRate = TopLevel.circuit.default.regulatingRate
    internal var bindingRate: DslRate = TopLevel.circuit.default.bindingRate
    internal var unbindingRate: DslRate = TopLevel.circuit.default.unbindingRate

    override val modelReaction: Regulation
        get() = BasicRegulation(
            transcription.modelReaction,
            regulator.modelEntity,
            regulatingRate.value,
            bindingRate.value,
            unbindingRate.value
        )
}

class DslChemicalReaction internal constructor(
    private val reagents: Map<DslEntity, Int>,
    private val products: Map<DslEntity, Int>
) : DslReaction() {
    internal val rate = DslRate()

    override val modelReaction
        get() = BasicChemicalReaction(
            reagents.mapKeys { (entity, _) -> entity.modelEntity },
            products.mapKeys { (entity, _) -> entity.modelEntity },
            rate.value
        )
}