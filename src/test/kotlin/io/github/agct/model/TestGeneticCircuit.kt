package io.github.agct.model

import io.github.agct.DEGRADATION
import io.github.agct.DIRECT
import io.github.agct.DIRECT_REGULATION
import io.github.agct.GENE
import io.github.agct.MRNA
import io.github.agct.PROTEIN
import io.github.agct.REGULATED_GENE
import io.github.agct.REGULATED_MRNA
import io.github.agct.REGULATIVE
import io.github.agct.TRANSCRIPTION
import io.github.agct.TRANSCRIPTION_REGULATION
import io.github.agct.TRANSLATION
import io.github.agct.TRANSLATION_REGULATION
import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.matchers.collections.shouldHaveSingleElement
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import io.github.agct.model.circuit.BasicGeneticCircuit
import io.github.agct.model.entities.BasicGene
import io.github.agct.model.entities.entity
import io.github.agct.model.reactions.BasicTranscription
import io.github.agct.model.reactions.BasicTranslation
import io.github.agct.model.reactions.DirectTranscription

internal class TestGeneticCircuit : StringSpec({
    "test direct circuit" {
        BasicGeneticCircuit("Direct Circuit").run {
            addReactions(
                DEGRADATION,
                DIRECT,
                DIRECT_REGULATION
            )

            entities shouldContainExactlyInAnyOrder setOf(
                GENE,
                PROTEIN,
                REGULATIVE,
                REGULATED_GENE
            )
            reactions shouldContainExactlyInAnyOrder setOf(
                DEGRADATION,
                DIRECT,
                DIRECT_REGULATION
            )

            shouldThrow<IllegalArgumentException> {
                addReaction(DEGRADATION)
            }.message shouldBe "$DEGRADATION already set for $PROTEIN."

            shouldThrow<IllegalArgumentException> {
                addReaction(
                    DirectTranscription(
                        entity<BasicGene>(
                            "g"
                        ), PROTEIN
                    )
                )
            }.message shouldBe "$PROTEIN is already transcribed/translated by $GENE"
        }
    }

    "test mrna circuit" {
        BasicGeneticCircuit("Mrna Circuit").run {
            addReactions(
                DEGRADATION,
                TRANSCRIPTION,
                TRANSLATION,
                TRANSCRIPTION_REGULATION,
                TRANSLATION_REGULATION
            )

            entities shouldContainExactlyInAnyOrder setOf(
                GENE,
                MRNA,
                PROTEIN,
                REGULATIVE,
                REGULATED_GENE,
                REGULATED_MRNA
            )

            reactions shouldContainExactlyInAnyOrder setOf(
                DEGRADATION,
                TRANSCRIPTION,
                TRANSLATION,
                TRANSCRIPTION_REGULATION,
                TRANSLATION_REGULATION
            )

            shouldThrow<IllegalArgumentException> {
                addReaction(DEGRADATION)
            }.message shouldBe "$DEGRADATION already set for $PROTEIN."

            shouldThrow<IllegalArgumentException> {
                addReaction(
                    DirectTranscription(
                        entity("g"),
                        PROTEIN
                    )
                )
            }.message shouldBe "$PROTEIN is already transcribed/translated by $MRNA"

            shouldThrow<IllegalArgumentException> {
                addReaction(
                    BasicTranscription(
                        entity("g"),
                        MRNA
                    )
                )
            }.message shouldBe "$MRNA is already transcribed by $GENE"

            shouldThrow<IllegalArgumentException> {
                addReaction(
                    BasicTranslation(
                        entity("m"),
                        PROTEIN
                    )
                )
            }.message shouldBe "$PROTEIN is already transcribed/translated by $MRNA"

            shouldThrow<IllegalArgumentException> {
                addReaction(
                    BasicTranslation(
                        MRNA,
                        entity("p")
                    )
                )
            }.message shouldBe "$MRNA already translates $PROTEIN"
        }
    }

    "test export" {
        BasicGeneticCircuit("Mrna Circuit").run {
            addReactions(DIRECT)
            entities shouldContainExactlyInAnyOrder setOf(GENE, PROTEIN)
            reactions shouldHaveSingleElement DIRECT

            shouldThrow<IllegalStateException> {
                checkRules()
            }.message shouldBe "Degradation reaction not set for $PROTEIN"
        }
    }
})
