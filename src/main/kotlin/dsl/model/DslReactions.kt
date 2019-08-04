@file:Suppress("PackageDirectoryMismatch")

package dsl

import model.entities.DegradingEntity
import model.reactions.*
import model.reactions.BiochemicalReaction
import model.reactions.DirectTranscription
import kotlin.properties.Delegates

abstract class DslReaction internal constructor() {
    internal abstract val biochemicalReaction: BiochemicalReaction
}

class DslDegradation internal constructor(default: DefaultValues) : DslReaction() {
    internal lateinit var entity: DslDegradable
    internal var degradationRate: DslRate = default.degradationRate

    override val biochemicalReaction: BiochemicalReaction
        get() = BasicDegradation(
            entity.biochemicalEntity as DegradingEntity,
            degradationRate.value
        )
}

class DslTranscription internal constructor(default: DefaultValues) : DslReaction() {
    internal lateinit var coder: DslGene
    internal lateinit var target: DslProtein
    internal var basalRate: DslRate = default.basalRate

    override val biochemicalReaction: DirectTranscription
        get() = DirectTranscription(
            coder.biochemicalEntity,
            target.biochemicalEntity,
            basalRate.value
        )
}

class DslRegulation internal constructor(default: DefaultValues): DslReaction() {
    internal lateinit var transcription: DslTranscription
    internal lateinit var regulator: DslRegulating
    internal var regulatedRate: DslRate = default.regulatedRate
    internal var bindingRate: DslRate = default.bindingRate
    internal var unbindingRate: DslRate = default.unbindingRate

    override val biochemicalReaction: Regulation
        get() = BasicRegulation(
            transcription.biochemicalReaction,
            regulator.biochemicalEntity,
            regulatedRate.value,
            bindingRate.value,
            unbindingRate.value
        )
}