@file:Suppress("PackageDirectoryMismatch")

package dsl

import model.reactions.*
import model.reactions.BiochemicalReaction
import model.reactions.DirectTranscription

abstract class DslReaction internal constructor() {
    internal abstract val biochemicalReaction: BiochemicalReaction
}

class DslTranscription internal constructor(
    private val coder: DslGene,
    private val target: DslProtein
) : DslReaction() {
    internal var basalRate: DslRate = TopLevel.circuit.default.basalRate

    override val biochemicalReaction: DirectTranscription
        get() = DirectTranscription(
            coder.biochemicalEntity,
            target.biochemicalEntity,
            basalRate.value
        )
}

class DslRegulation internal constructor(
    private val transcription: DslTranscription,
    private val regulator: DslRegulating
) : DslReaction() {
    internal var regulatedRate: DslRate = TopLevel.circuit.default.regulatedRate
    internal var bindingRate: DslRate = TopLevel.circuit.default.bindingRate
    internal var unbindingRate: DslRate = TopLevel.circuit.default.unbindingRate

    override val biochemicalReaction: Regulation
        get() = BasicRegulation(
            transcription.biochemicalReaction,
            regulator.biochemicalEntity,
            regulatedRate.value,
            bindingRate.value,
            unbindingRate.value
        )
}