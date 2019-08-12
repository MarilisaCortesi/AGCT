@file:Suppress("PackageDirectoryMismatch", "PropertyName")

package dsl

open class ReactionLevel<out R : DslReaction> internal constructor(
    protected val circuit: DslCircuit,
    protected val reaction: R
)

class TranscriptionLevel internal constructor(circuit: DslCircuit, transcription: DslTranscription) :
    ReactionLevel<DslTranscription>(circuit, transcription) {
    val the
        get() = The()

    val with
        get() = With()

    val being
        get() = Being()

    inner class The internal constructor() {
        infix fun protein(id: String) =
            circuit.getOrPutEntity(id) { DslProtein(this) }.let { protein ->
                EntityLevelWrapper(ProteinLevel(circuit, protein))
        }
    }

    inner class With internal constructor() {
        infix fun a(dummy: basal_rate) = reaction.basalRate
    }

    inner class Being internal constructor() {
        infix fun regulatedBy(block: RegulationLevel.() -> Unit) = Unit
    }
}

class RegulationLevel internal constructor(circuit: DslCircuit, regulation: DslRegulation) :
    ReactionLevel<DslRegulation>(circuit, regulation) {
    val the
        get() = The()

    val with
        get() = With()

    inner class The internal constructor() {
        infix fun protein(id: String) =
            circuit.getOrPutEntity(id) { DslProtein(this) }.let { protein ->
                EntityLevelWrapper(ProteinLevel(circuit, protein))
            }

        infix fun molecule(id: String) =
            circuit.getOrPutEntity(id) { DslMolecule(this) }.let { molecule ->
                EntityLevelWrapper(RegulatorLevel(circuit, molecule))
            }
    }

    inner class With internal constructor() {
        infix fun a(dummy: regulated_rate) = reaction.regulatedRate

        infix fun a(dummy: binding_rate) = reaction.bindingRate

        infix fun an(dummy: unbinding_rate) = reaction.unbindingRate
    }
}