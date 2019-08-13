@file:Suppress("PackageDirectoryMismatch", "UNUSED_PARAMETER", "FunctionName")

package dsl

class EntityWrapper internal constructor(private val entity: String) {
    private val entityLevel = GenericEntityLevel(entity)

    infix fun having(block: GenericEntityLevel.() -> Unit) =
        entityLevel.run(block)
}

class GeneWrapper internal constructor(private val gene: String) {
    private val geneLevel = GeneLevel(gene)

    infix fun having(block: GeneLevel.() -> Unit) =
        geneLevel.run {
            block()
            AfterHaving()
        }

    infix fun that(dummy: codes) =
        Codes()

    inner class AfterHaving internal constructor() {
        infix fun that(dummy: codes) =
            this@GeneWrapper.that(dummy)
    }

    inner class Codes internal constructor() {
        infix fun For(block: CodingLevel.() -> Unit) =
            CodingLevel(geneLevel.entity).block()
    }
}

class TranscriptionWrapper internal constructor(private val coder: DslGene, target: String) {
    private val proteinLevel = ProteinLevel(target)

    private val transcriptionLevel = TranscriptionLevel(coder, proteinLevel.entity)

    infix fun having(block: ProteinLevel.() -> Unit) =
        proteinLevel.run {
            block()
            AfterHaving()
        }

    infix fun with(block: TranscriptionLevel.() -> Unit) =
        transcriptionLevel.run {
            block()
            AfterWith()
        }

    infix fun being(dummy: regulated) =
        Regulated()

    inner class AfterHaving internal constructor() {
        infix fun with(block: TranscriptionLevel.() -> Unit) =
            this@TranscriptionWrapper.with(block)

        infix fun being(dummy: regulated) =
            this@TranscriptionWrapper.being(dummy)
    }

    inner class AfterWith internal constructor() {
        infix fun being(dummy: regulated) =
            this@TranscriptionWrapper.being(dummy)
    }

    inner class Regulated internal constructor() {
        infix fun by(block: RegulatingLevel.() -> Unit) =
            RegulatingLevel(transcriptionLevel.reaction).block()
    }
}

class RegulationWrapper internal constructor(private val transcription: DslTranscription, regulator: String) {
    private val regulatorLevel = RegulatorLevel(regulator)

    private val regulationLevel = RegulationLevel(transcription, regulatorLevel.entity)

    infix fun having(block: RegulatorLevel.() -> Unit) =
        regulatorLevel.run {
            block()
            AfterHaving()
        }

    infix fun with(block: RegulationLevel.() -> Unit) =
        regulationLevel.block()

    inner class AfterHaving internal constructor() {
        infix fun with(block: RegulationLevel.() -> Unit) =
            this@RegulationWrapper.with(block)
    }
}