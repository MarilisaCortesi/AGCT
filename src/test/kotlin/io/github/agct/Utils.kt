package io.github.agct

import io.github.agct.model.entities.BasicDegradingEntity
import io.github.agct.model.entities.BasicEntity
import io.github.agct.model.entities.BasicGene
import io.github.agct.model.entities.BasicMRna
import io.github.agct.model.entities.BasicProtein
import io.github.agct.model.entities.BasicRegulatingEntity
import io.github.agct.model.entities.DegradingRegulatingEntity
import io.github.agct.model.entities.RegulatedGene
import io.github.agct.model.entities.RegulatedMRna
import io.github.agct.model.entities.entity
import io.github.agct.model.reactions.BasicDegradation
import io.github.agct.model.reactions.BasicRegulation
import io.github.agct.model.reactions.BasicTranscription
import io.github.agct.model.reactions.BasicTranslation
import io.github.agct.model.reactions.DirectTranscription
import io.kotlintest.Matcher
import io.kotlintest.Result
import io.kotlintest.fail
import io.kotlintest.matchers.collections.shouldHaveSize
import kotlin.math.abs

internal val ENTITY = entity<BasicEntity>("ent")
internal val DEGRADING = entity<BasicDegradingEntity>("det")
internal val REGULATIVE =
    entity<BasicRegulatingEntity>("reg")
internal val DEGRADING_REGULATIVE =
    entity<DegradingRegulatingEntity>("det reg")
internal val GENE = entity<BasicGene>("gen")
internal val MRNA = entity<BasicMRna>("rna")
internal val PROTEIN = entity<BasicProtein>("pro")
internal val REGULATED_GENE =
    RegulatedGene(GENE, REGULATIVE)
internal val REGULATED_MRNA =
    RegulatedMRna(MRNA, REGULATIVE)

internal val DEGRADATION = BasicDegradation(PROTEIN)
internal val DIRECT =
    DirectTranscription(GENE, PROTEIN)
internal val TRANSCRIPTION =
    BasicTranscription(GENE, MRNA)
internal val TRANSLATION =
    BasicTranslation(MRNA, PROTEIN)
internal val DIRECT_REGULATION =
    BasicRegulation(DIRECT, REGULATIVE)
internal val TRANSCRIPTION_REGULATION =
    BasicRegulation(TRANSCRIPTION, REGULATIVE)
internal val TRANSLATION_REGULATION =
    BasicRegulation(TRANSLATION, REGULATIVE)

private const val DELTA = 10e-15

internal infix fun<T : Number> Collection<T>.shouldMatchWithTolerance(expected: Collection<T>) {
    this shouldHaveSize expected.size
    forEachIndexed { i, v ->
        if (abs(v.toDouble() - expected.elementAt(i).toDouble()) > DELTA)
            fail("Collection should have value ${expected.elementAt(i)} in position $i but it has $v")
    }
}

internal infix fun<T : Number> Sequence<T>.shouldMatchWithTolerance(expected: Sequence<T>) =
    toList().shouldMatchWithTolerance(expected.toList())

internal infix fun<T> T.shouldEqual(expected: Any?) {
    if (this != expected) fail("$this and $expected are not the same but they should be")
}

internal infix fun<T> T.shouldNotEqual(expected: Any?) {
    if (this == expected) fail("$this and $expected are the same but they shouldn't be")
}

internal val Int.elements : Matcher<Collection<*>>
    get() = object : Matcher<Collection<*>> {
        override fun test(value: Collection<*>) =
            value.size.run {
                Result(
                    this == this@elements,
                    "map should have ${ this@elements} model.elements but it has $this",
                    "map should not have ${ this@elements} model.elements but it has"
                )
            }
    }