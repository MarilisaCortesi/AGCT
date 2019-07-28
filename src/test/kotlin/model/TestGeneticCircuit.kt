package model

import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import model.circuit.BasicGeneticCircuit
import model.entities.BasicGene
import model.entities.entity
import model.reactions.DirectTranscription

internal class TestGeneticCircuit : StringSpec({
    BasicGeneticCircuit("test circuit").run {
        add(DEGRADATION, DIRECT_TRANSCRIPTION, REGULATION)

        entities shouldContainExactlyInAnyOrder setOf(GENE, PROTEIN, REGULATIVE, REGULATED_GENE)
        reactions shouldContainExactlyInAnyOrder setOf(DEGRADATION, DIRECT_TRANSCRIPTION, REGULATION)

        shouldThrow<IllegalArgumentException> {
            add(DEGRADATION)
        }.message shouldBe "$DEGRADATION already set for $PROTEIN."

        shouldThrow<IllegalArgumentException> {
            add(DirectTranscription(entity<BasicGene>("g"), PROTEIN))
        }.message shouldBe "Transcription reaction already set for $PROTEIN."
    }
})
