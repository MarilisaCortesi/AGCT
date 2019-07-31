package dsl.model

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
    internal var degradationRate: DslRate = DslRate()

    override val biochemicalReaction: BiochemicalReaction
        get() = BasicDegradation(
            entity.biochemicalEntity as DegradingEntity,
            degradationRate.rate
        )
}

class DslTranscription internal constructor() : DslReaction() {
    internal var coder: DslGene by Delegates.notNull()
    internal var target: DslProtein by Delegates.notNull()
    internal var transcriptionRate: DslRate = DslRate()

    override val biochemicalReaction: DirectTranscription
        get() = DirectTranscription(
            coder.biochemicalEntity,
            target.biochemicalEntity,
            transcriptionRate.rate
        )
}

class DslRegulation internal constructor(): DslReaction() {
    internal var transcription: DslTranscription by Delegates.notNull()
    internal var regulator: DslRegulating by Delegates.notNull()
    internal var regulatedRate: DslRate = DslRate()
    internal var bindingRate: DslRate = DslRate()
    internal var unbindingRate: DslRate = DslRate()

    override val biochemicalReaction: Regulation
        get() = BasicRegulation(
            transcription.biochemicalReaction,
            regulator.biochemicalEntity,
            regulatedRate.rate,
            bindingRate.rate,
            unbindingRate.rate
        )
}