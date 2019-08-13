@file:Suppress("PackageDirectoryMismatch")

package dsl

import dsl.TopLevel.Companion.circuit

class ContainingLevel internal constructor() {
    val the
        get() = this

    infix fun gene(id: String) =
        GeneWrapper(id)
}

class CodingLevel(private val coder: DslGene) {
    val the
        get() = The()

    inner class The internal constructor() {
        infix fun protein(id: String) =
            TranscriptionWrapper(coder, id)
    }
}

class RegulatingLevel(private val transcription: DslTranscription) {
    val the
        get() = The()

    inner class The internal constructor() {
        infix fun molecule(id: String) =
            RegulationWrapper(transcription, id)

        infix fun protein(id: String) =
            molecule(circuit.getOrPutEntity(id) { DslProtein(this) }.id)
    }
}