@file:Suppress("PackageDirectoryMismatch", "PropertyName")

package dsl

import java.lang.IllegalStateException

abstract class ReactionLevel<R : DslReaction> internal constructor() {
    protected abstract val circuit: DslCircuit
    protected abstract val reaction: R
}

class TranscriptionLevel internal constructor(
    override val circuit: DslCircuit,
    private val coder: DslGene
) : ReactionLevel<DslTranscription>() {
    private lateinit var transcription: DslTranscription

    override val reaction
        get() = if (this::transcription.isInitialized) transcription
                else throw IllegalStateException("The target protein has not been set yet.")

    val the
        get() = The()

    val with
        get() = With()

    val regulatedBy
        get() = RegulatedBy()

    inner class The internal constructor() {
        infix fun protein(id: String) =
            ProteinLevel(circuit, id).run {
                transcription = DslTranscription(coder, entity).apply { circuit.putReaction(this) }
                EntityLevelWrapper(this)
            }
    }

    inner class With internal constructor() {
        infix fun a(dummy: basal_rate) = reaction.basalRate
    }

    inner class RegulatedBy internal constructor() {
        operator fun invoke(block: RegulationLevel.() -> Unit) =
            RegulationLevel(circuit, transcription).block()
    }
}

class RegulationLevel internal constructor(
    override val circuit: DslCircuit,
    private val transcription: DslTranscription
) : ReactionLevel<DslRegulation>() {
    private lateinit var regulation: DslRegulation

    override val reaction
        get() = if (this::regulation.isInitialized) regulation
                else throw IllegalStateException("The regulator has not been set yet.")

    val the
        get() = The()

    val with
        get() = With()

    inner class The internal constructor() {
        infix fun protein(id: String) =
            ProteinLevel(circuit, id).wrapper

        infix fun molecule(id: String) =
            RegulatorLevel(circuit, id).wrapper

        private val<E : EntityLevel<DslRegulating>> E.wrapper
            get() = run {
                regulation = DslRegulation(transcription, entity).apply { circuit.putReaction(this) }
                EntityLevelWrapper(this)
            }
    }

    inner class With internal constructor() {
        infix fun a(dummy: regulated_rate) = reaction.regulatedRate

        infix fun a(dummy: binding_rate) = reaction.bindingRate

        infix fun an(dummy: unbinding_rate) = reaction.unbindingRate
    }
}