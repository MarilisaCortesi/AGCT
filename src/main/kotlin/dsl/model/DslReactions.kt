@file:Suppress("PackageDirectoryMismatch")

package dsl

import model.reactions.*
import model.reactions.GeneticReaction
import model.reactions.DirectTranscription

abstract class DslReaction internal constructor() {
    internal abstract val geneticReaction: GeneticReaction
}

class DslTranscription internal constructor(
    private val coder: DslGene,
    private val target: DslProtein
) : DslReaction() {
    internal var basalRate: DslRate = TopLevel.circuit.default.basalRate

    override val geneticReaction: DirectTranscription
        get() = DirectTranscription(
            coder.geneticEntity,
            target.geneticEntity,
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

    override val geneticReaction: Regulation
        get() = BasicRegulation(
            transcription.geneticReaction,
            regulator.geneticEntity,
            regulatingRate.value,
            bindingRate.value,
            unbindingRate.value
        )
}