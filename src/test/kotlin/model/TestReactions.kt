package model

import io.kotlintest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotlintest.matchers.collections.shouldHaveSingleElement
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import model.entities.*
import model.reactions.*
import model.variables.BasicRate

internal class TestReactions : StringSpec({
    "test basics" {
        DEGRADATION.run {
            molecule shouldBe PROTEIN
            degradationRate shouldBe BasicRate()
            reactions shouldContainExactlyInAnyOrder DEGRADATION_REACTIONS
            reactions.flatMap { it.reagents.values + it.products.values }.distinct() shouldHaveSingleElement 1
        }

        DIRECT.run {
            coder shouldBe GENE
            target shouldBe PROTEIN
            basalRate shouldBe BasicRate()
            reactions shouldContainExactlyInAnyOrder DIRECT_REACTIONS
            reactions.flatMap { it.reagents.values + it.products.values }.distinct() shouldHaveSingleElement 1
        }

        TRANSCRIPTION.run {
            coder shouldBe GENE
            target shouldBe MRNA
            basalRate shouldBe BasicRate()
            reactions shouldContainExactlyInAnyOrder TRANSCRIPTION_REACTIONS
            reactions.flatMap { it.reagents.values + it.products.values }.distinct() shouldHaveSingleElement 1
        }

        TRANSLATION.run {
            coder shouldBe MRNA
            target shouldBe PROTEIN
            basalRate shouldBe BasicRate()
            reactions shouldContainExactlyInAnyOrder TRANSLATION_REACTIONS
            reactions.flatMap { it.reagents.values + it.products.values }.distinct() shouldHaveSingleElement 1
        }

        DIRECT_REGULATION.run {
            reaction shouldBe DIRECT
            regulator shouldBe REGULATIVE
            regulatingRate shouldBe BasicRate()
            unbindingRate shouldBe BasicRate()
            bindingRate shouldBe BasicRate()
            reactions shouldContainExactlyInAnyOrder DIRECT_REGULATION_REACTIONS
            reactions.flatMap { it.reagents.values + it.products.values }.distinct() shouldHaveSingleElement 1
        }

        TRANSCRIPTION_REGULATION.run {
            reaction shouldBe TRANSCRIPTION
            regulator shouldBe REGULATIVE
            regulatingRate shouldBe BasicRate()
            unbindingRate shouldBe BasicRate()
            bindingRate shouldBe BasicRate()
            reactions shouldContainExactlyInAnyOrder TRANSCRIPTION_REGULATION_REACTIONS
            reactions.flatMap { it.reagents.values + it.products.values }.distinct() shouldHaveSingleElement 1
        }

        TRANSLATION_REGULATION.run {
            reaction shouldBe TRANSLATION
            regulator shouldBe REGULATIVE
            regulatingRate shouldBe BasicRate()
            unbindingRate shouldBe BasicRate()
            bindingRate shouldBe BasicRate()
            reactions shouldContainExactlyInAnyOrder TRANSLATION_REGULATION_REACTIONS
            reactions.flatMap { it.reagents.values + it.products.values }.distinct() shouldHaveSingleElement 1
        }
    }

    "test equality" {
        DEGRADATION shouldEqual BasicDegradation(PROTEIN, BasicRate(2.0))
        DIRECT shouldEqual DirectTranscription(GENE, PROTEIN, BasicRate(2.0))
        TRANSCRIPTION shouldEqual BasicTranscription(GENE, MRNA, BasicRate(2.0))
        TRANSLATION shouldEqual BasicTranslation(MRNA, PROTEIN, BasicRate(2.0))
        DIRECT_REGULATION shouldEqual BasicRegulation(DIRECT, REGULATIVE, BasicRate(2.0), BasicRate(2.0), BasicRate(2.0))
        TRANSCRIPTION_REGULATION shouldEqual BasicRegulation(TRANSCRIPTION, REGULATIVE, BasicRate(2.0), BasicRate(2.0), BasicRate(2.0))
        TRANSLATION_REGULATION shouldEqual BasicRegulation(TRANSLATION, REGULATIVE, BasicRate(2.0), BasicRate(2.0), BasicRate(2.0))

        val gene = entity<Gene>("g2")
        val mrna = entity<MRna>("m2")
        val protein = entity<Protein>("p2")
        val regulative = entity<RegulatingEntity>("r2")
        DEGRADATION shouldNotEqual BasicDegradation(protein)
        DIRECT shouldNotEqual DirectTranscription(GENE, protein)
        DIRECT shouldNotEqual DirectTranscription(gene, PROTEIN)
        TRANSCRIPTION shouldNotEqual BasicTranscription(GENE, mrna)
        TRANSCRIPTION shouldNotEqual BasicTranscription(gene, MRNA)
        TRANSLATION shouldNotEqual BasicTranslation(MRNA, protein)
        TRANSLATION shouldNotEqual BasicTranslation(mrna, PROTEIN)
        DIRECT_REGULATION shouldNotEqual BasicRegulation(DIRECT, regulative)
        DIRECT_REGULATION shouldNotEqual BasicRegulation(DirectTranscription(gene, protein), REGULATIVE)
        TRANSCRIPTION_REGULATION shouldNotEqual BasicRegulation(TRANSCRIPTION, regulative)
        TRANSCRIPTION_REGULATION shouldNotEqual BasicRegulation(BasicTranscription(gene, mrna), REGULATIVE)
        TRANSLATION_REGULATION shouldNotEqual BasicRegulation(TRANSLATION, regulative)
        TRANSLATION_REGULATION shouldNotEqual BasicRegulation(BasicTranslation(mrna, protein), REGULATIVE)
    }
})

private val DEGRADATION_REACTIONS = setOf(
    BasicSingleReaction(
        reagents = mapOf(PROTEIN to 1),
        products = emptyMap(),
        name = "pro degradation"
    )
)

private val DIRECT_REACTIONS = setOf(
    BasicSingleReaction(
        reagents = mapOf(GENE to 1),
        products = mapOf(GENE to 1, PROTEIN to 1),
        name = "pro transcription"
    )
)

private val TRANSCRIPTION_REACTIONS = setOf(
    BasicSingleReaction(
        reagents = mapOf(GENE to 1),
        products = mapOf(GENE to 1, MRNA to 1),
        name = "rna transcription"
    )
)

private val TRANSLATION_REACTIONS = setOf(
    BasicSingleReaction(
        reagents = mapOf(MRNA to 1),
        products = mapOf(MRNA to 1, PROTEIN to 1),
        name = "pro translation"
    )
)

private val DIRECT_REGULATION_REACTIONS = setOf(
    BasicSingleReaction(
        reagents = mapOf(GENE to 1, REGULATIVE to 1),
        products = mapOf(REGULATED_GENE to 1),
        name = "gen binding"
    ),
    BasicSingleReaction(
        reagents = mapOf(REGULATED_GENE to 1),
        products = mapOf(GENE to 1, REGULATIVE to 1),
        name = "gen unbinding"
    ),
    BasicSingleReaction(
        reagents = mapOf(REGULATED_GENE to 1),
        products = mapOf(REGULATED_GENE to 1, PROTEIN to 1),
        name = "pro transcription"
    )
)

private val TRANSCRIPTION_REGULATION_REACTIONS = setOf(
    BasicSingleReaction(
        reagents = mapOf(GENE to 1, REGULATIVE to 1),
        products = mapOf(REGULATED_GENE to 1),
        name = "gen binding"
    ),
    BasicSingleReaction(
        reagents = mapOf(REGULATED_GENE to 1),
        products = mapOf(GENE to 1, REGULATIVE to 1),
        name = "gen unbinding"
    ),
    BasicSingleReaction(
        reagents = mapOf(REGULATED_GENE to 1),
        products = mapOf(REGULATED_GENE to 1, MRNA to 1),
        name = "rna transcription"
    )
)

private val TRANSLATION_REGULATION_REACTIONS = setOf(
    BasicSingleReaction(
        reagents = mapOf(MRNA to 1, REGULATIVE to 1),
        products = mapOf(REGULATED_MRNA to 1),
        name = "rna binding"
    ),
    BasicSingleReaction(
        reagents = mapOf(REGULATED_MRNA to 1),
        products = mapOf(MRNA to 1, REGULATIVE to 1),
        name = "rna unbinding"
    ),
    BasicSingleReaction(
        reagents = mapOf(REGULATED_MRNA to 1),
        products = mapOf(REGULATED_MRNA to 1, PROTEIN to 1),
        name = "pro transcription"
    )
)
