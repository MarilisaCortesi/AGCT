package dsl.model

import model.reactions.*
import model.reactions.BiochemicalReaction
import model.reactions.DirectTranscription
import model.reactions.Transcription
import model.variables.Rate
import kotlin.properties.Delegates

abstract class DslReaction internal constructor() {
    internal abstract val biochemicalReaction: BiochemicalReaction
}

class DslTranscription internal constructor() : DslReaction() {
    private var coder: DslGene by Delegates.notNull()
    private var target: DslProtein by Delegates.notNull()
    private var transcriptionRate: Rate = Rate()

    override val biochemicalReaction: DirectTranscription
        get() = DirectTranscription(
            coder.biochemicalEntity,
            target.biochemicalEntity,
            transcriptionRate
        )
}

class DslRegulation internal constructor(): DslReaction() {
    private var transcription: DslTranscription by Delegates.notNull()
    private var regulator: DslDegradingRegulating by Delegates.notNull()
    private var regulatedRate: Rate = Rate()
    private var bindingRate: Rate = Rate()
    private var unbindingRate: Rate = Rate()

    override val biochemicalReaction: Regulation
        get() = BasicRegulation(
            transcription.biochemicalReaction,
            regulator.biochemicalEntity,
            regulatedRate,
            bindingRate,
            unbindingRate
        )
}