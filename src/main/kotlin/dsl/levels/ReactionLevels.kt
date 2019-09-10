@file:Suppress("PackageDirectoryMismatch", "PropertyName", "UNUSED_PARAMETER")

package dsl

import dsl.TopLevel.Companion.circuit
import model.utils.lateVal

abstract class ReactionLevel<R : DslReaction> internal constructor() {
    internal abstract val reaction: R
}

class TranscriptionLevel internal constructor(private val coder: DslGene) :
    ReactionLevel<DslTranscription>() {
    private var transcription: DslTranscription by lateVal("target protein")

    override val reaction
        get() = transcription

    val the
        get() = this

    val with
        get() = With()

    val regulated
        get() = RegulatedBy()

    infix fun protein(id: String) =
        ProteinLevel(id).run {
            transcription = DslTranscription(coder, entity).apply { circuit.putReaction(this) }
            EntityLevelWrapper(this)
        }

    inner class With internal constructor() {
        infix fun a(dummy: basal.Rate) = reaction.basalRate
    }

    inner class RegulatedBy internal constructor() {
        infix fun by(block: RegulationLevel.() -> Unit) = And().and(block)

        inner class And internal constructor() {
            infix fun and(block: RegulationLevel.() -> Unit) =
                RegulationLevel(transcription).block().let { this }
        }
    }
}

class RegulationLevel internal constructor(
    private val transcription: DslTranscription
) : ReactionLevel<DslRegulation>() {
    private var regulation: DslRegulation by lateVal("regulator")

    override val reaction
        get() = regulation

    val the
        get() = this

    val with
        get() = With()

    infix fun protein(id: String) =
        ProteinLevel(id).wrapper

    infix fun molecule(id: String) =
        RegulatorLevel(id).wrapper

    private val<E : EntityLevel<DslRegulating>> E.wrapper
        get() = run {
            regulation = DslRegulation(transcription, entity).apply { circuit.putReaction(this) }
            EntityLevelWrapper(this)
        }

    inner class With internal constructor() {
        infix fun a(dummy: regulating.Rate) = reaction.regulatingRate
        infix fun a(dummy: binding.Rate) = reaction.bindingRate
        infix fun an(dummy: unbinding.Rate) = reaction.unbindingRate
    }
}