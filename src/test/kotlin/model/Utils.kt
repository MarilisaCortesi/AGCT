package model

import io.kotlintest.Matcher
import io.kotlintest.Result
import io.kotlintest.fail
import io.kotlintest.matchers.collections.shouldHaveSize
import model.entities.*
import model.reactions.*
import kotlin.math.abs

internal val MOLECULE = entity<Molecule>("mol")
internal val DEGRADING = entity<DegradingMolecule>("det")
internal val REGULATIVE = entity<RegulatingMolecule>("reg")
internal val DEGRADING_REGULATIVE = entity<DegradingRegulatingMolecule>("det reg")
internal val GENE = entity<Gene>("gen")
internal val MRNA = entity<MRna>("rna")
internal val PROTEIN = entity<Protein>("pro")
internal val REGULATED_GENE = RegulatedGene(GENE, REGULATIVE)

internal val DEGRADATION = BasicDegradation(PROTEIN)
internal val DIRECT_TRANSCRIPTION = DirectTranscription(GENE, PROTEIN)
internal val TWO_STEP_TRANSCRIPTION = TwoStepTranscription(GENE, MRNA, PROTEIN)
internal val REGULATION = BasicRegulation(DIRECT_TRANSCRIPTION, REGULATIVE)

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

/*
val Int.elements : Matcher<Map<*, *>>
    get() = object : Matcher<Map<*, *>> {
        override fun test(value: Map<*, *>) =
            value.size.run {
                Result(
                    this == this@elements,
                    "map should have ${ this@elements} model.elements but it has $this",
                    "map should not have ${ this@elements} model.elements but it has"
                )
            }
    }
*/