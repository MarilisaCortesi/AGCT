package model

import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.matchers.collections.shouldHaveSingleElement
import io.kotlintest.shouldBe
import io.kotlintest.shouldHave
import io.kotlintest.specs.StringSpec
import model.entities.*
import model.reactions.*
import model.variables.Rate

private val DEGRADATION_REACTIONS = setOf(
    BasicReaction(
        reagents = mapOf(PROTEIN to 1),
        products = emptyMap(),
        name = "pro degradation"
    )
)

private val DIRECT_TRANSCRIPTION_REACTIONS = setOf(
    BasicReaction(
        reagents = mapOf(GENE to 1),
        products = mapOf(GENE to 1, PROTEIN to 1),
        name = "pro transcription"
    )
)

private val TWO_STEP_TRANSCRIPTION_REACTIONS = setOf(
    BasicReaction(
        reagents = mapOf(GENE to 1),
        products = mapOf(GENE to 1, MRNA to 1),
        name = "rna transcription"
    ),
    BasicReaction(
        reagents = mapOf(MRNA to 1),
        products = mapOf(MRNA to 1, PROTEIN to 1),
        name = "pro translation"
    )
)

private val REGULATION_REACTIONS = setOf(
    BasicReaction(
        reagents = mapOf(GENE to 1, REGULATIVE to 1),
        products = mapOf(REGULATED_GENE to 1),
        name = "gen binding"
    ),
    BasicReaction(
        reagents = mapOf(REGULATED_GENE to 1),
        products = mapOf(GENE to 1, REGULATIVE to 1),
        name = "gen unbinding"
    ),
    BasicReaction(
        reagents = mapOf(REGULATED_GENE to 1),
        products = mapOf(REGULATED_GENE to 1, PROTEIN to 1),
        name = "pro transcription"
    )
)

internal class TestReactions : StringSpec({
    "test basics" {
        DEGRADATION.run {
            molecule shouldBe PROTEIN
            degradationRate shouldBe Rate()
            reactions shouldContainExactlyInAnyOrder DEGRADATION_REACTIONS
            reactions.flatMap { it.reagents.values + it.products.values }.distinct() shouldHaveSingleElement 1
        }

        DIRECT_TRANSCRIPTION.run {
            coder shouldBe GENE
            step shouldBe null
            target shouldBe PROTEIN
            transcriptionRate shouldBe Rate()
            translationRate shouldBe null
            reactions shouldContainExactlyInAnyOrder DIRECT_TRANSCRIPTION_REACTIONS
            reactions.flatMap { it.reagents.values + it.products.values }.distinct() shouldHaveSingleElement 1
        }

        TWO_STEP_TRANSCRIPTION.run {
            coder shouldBe GENE
            step shouldBe MRNA
            target shouldBe PROTEIN
            transcriptionRate shouldBe Rate()
            translationRate shouldBe Rate()
            reactions shouldContainExactlyInAnyOrder TWO_STEP_TRANSCRIPTION_REACTIONS
            reactions.flatMap { it.reagents.values + it.products.values }.distinct() shouldHaveSingleElement 1
        }

        REGULATION.run {
            reaction shouldBe DIRECT_TRANSCRIPTION
            regulator shouldBe REGULATIVE
            regulatedRate shouldBe Rate()
            unbindingRate shouldBe Rate()
            bindingRate shouldBe Rate()
            reactions shouldContainExactlyInAnyOrder REGULATION_REACTIONS
            reactions.flatMap { it.reagents.values + it.products.values }.distinct() shouldHaveSingleElement 1
        }
    }

    "test equality" {
        DEGRADATION shouldEqual BasicDegradation(PROTEIN, Rate(2))
        DIRECT_TRANSCRIPTION shouldEqual DirectTranscription(GENE, PROTEIN, Rate(2))
        TWO_STEP_TRANSCRIPTION shouldEqual TwoStepTranscription(GENE, MRNA, PROTEIN, Rate(2), Rate(2))
        REGULATION shouldEqual BasicRegulation(DIRECT_TRANSCRIPTION, REGULATIVE, Rate(2), Rate(2))

        val gene = entity<Gene>("g2")
        val mrna = entity<MRna>("m2")
        val protein = entity<Protein>("p2")
        val regulative = entity<RegulatingMolecule>("r2")
        DEGRADATION shouldNotEqual BasicDegradation(protein)
        DIRECT_TRANSCRIPTION shouldNotEqual DirectTranscription(GENE, protein)
        DIRECT_TRANSCRIPTION shouldNotEqual DirectTranscription(gene, PROTEIN)
        TWO_STEP_TRANSCRIPTION shouldNotEqual TwoStepTranscription(GENE, MRNA, protein)
        TWO_STEP_TRANSCRIPTION shouldNotEqual TwoStepTranscription(GENE, mrna, PROTEIN)
        TWO_STEP_TRANSCRIPTION shouldNotEqual TwoStepTranscription(gene, MRNA, PROTEIN)
        REGULATION shouldNotEqual BasicRegulation(DIRECT_TRANSCRIPTION, regulative)
        REGULATION shouldNotEqual BasicRegulation(DirectTranscription(gene, protein), REGULATIVE)
    }
})
