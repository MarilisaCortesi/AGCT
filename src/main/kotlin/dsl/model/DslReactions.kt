@file:Suppress("PackageDirectoryMismatch")

package dsl

import model.reactions.*
import model.reactions.BiochemicalReaction
import model.reactions.DirectTranscription

abstract class DslReaction internal constructor() {
    internal abstract val biochemicalReaction: BiochemicalReaction
}

class DslTranscription internal constructor() : DslReaction() {
    internal lateinit var coder: DslGene
    internal lateinit var target: DslProtein
    internal var basalRate: DslRate = TopLevel.circuit.default.basalRate

    override val biochemicalReaction: DirectTranscription
        get() = DirectTranscription(
            coder.biochemicalEntity,
            target.biochemicalEntity,
            basalRate.value
        )
}

class DslRegulation internal constructor() : DslReaction() {
    internal lateinit var transcription: DslTranscription
    internal lateinit var regulator: DslRegulating
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