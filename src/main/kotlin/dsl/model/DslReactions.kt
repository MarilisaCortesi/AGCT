package dsl.model

import dsl.levels.TopLevel.Circuit.default
import model.entities.DegradingEntity
import model.reactions.*
import model.reactions.BiochemicalReaction
import model.reactions.DirectTranscription
import kotlin.properties.Delegates

abstract class DslReaction internal constructor() {
    internal abstract val biochemicalReaction: BiochemicalReaction
}

class DslDegradation internal constructor() : DslReaction() {
    internal var entity: DslDegradable by Delegates.notNull()
    internal var degradationRate: DslRate = default.degradationRate.copy

    override val biochemicalReaction: BiochemicalReaction
        get() = BasicDegradation(
            entity.biochemicalEntity as DegradingEntity,
            degradationRate.rate
        )
}

class DslTranscription internal constructor() : DslReaction() {
    internal var coder: DslGene by Delegates.notNull()
    internal var target: DslProtein by Delegates.notNull()
    internal var basalRate: DslRate = default.basalRate.copy

    override val biochemicalReaction: DirectTranscription
        get() = DirectTranscription(
            coder.biochemicalEntity,
            target.biochemicalEntity,
            basalRate.rate
        )
}

class DslRegulation internal constructor(): DslReaction() {
    internal var transcription: DslTranscription by Delegates.notNull()
    internal var regulator: DslRegulating by Delegates.notNull()
    internal var regulatedRate: DslRate = default.regulatedRate.copy
    internal var bindingRate: DslRate = default.bindingRate.copy
    internal var unbindingRate: DslRate = default.unbindingRate.copy

    override val biochemicalReaction: Regulation
        get() = BasicRegulation(
            transcription.biochemicalReaction,
            regulator.biochemicalEntity,
            regulatedRate.rate,
            bindingRate.rate,
            unbindingRate.rate
        )
}