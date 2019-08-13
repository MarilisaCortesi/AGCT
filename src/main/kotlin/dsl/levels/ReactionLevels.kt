@file:Suppress("PackageDirectoryMismatch", "PropertyName", "UNUSED_PARAMETER")

package dsl

import dsl.TopLevel.Companion.circuit

abstract class ReactionLevel<R : DslReaction> internal constructor() {
    internal abstract val reaction: R
}

class TranscriptionLevel internal constructor(coder: DslGene, target: DslProtein) :
    ReactionLevel<DslTranscription>() {
    override val reaction = DslTranscription(coder, target).apply { circuit.putReaction(this) }

    val a
        get() = A()

    inner class A internal constructor() {
        infix fun basal(dummy: rate) = reaction.basalRate
    }
}

class RegulationLevel internal constructor(transcription: DslTranscription, regulator: DslRegulating) :
    ReactionLevel<DslRegulation>() {
    override val reaction = DslRegulation(transcription, regulator).apply { circuit.putReaction(this) }

    val a
        get() = A()

    val an
        get() = An()

    inner class A internal constructor() {
        infix fun regulated(dummy: rate) = reaction.regulatedRate

        infix fun binding(dummy: rate) = reaction.bindingRate
    }

    inner class An internal constructor() {
        infix fun unbinding(dummy: rate) = reaction.unbindingRate
    }
}