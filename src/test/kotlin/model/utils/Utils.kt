package model.utils

import io.kotlintest.Matcher
import io.kotlintest.Result
import io.kotlintest.fail
import io.kotlintest.matchers.collections.shouldHaveSize
import model.entities.*
import kotlin.math.abs

val MOLECULE = BasicMolecule("mol")
val DETERIORATING = BasicDeterioratingMolecule("det")
val GENE = BasicGene("gen")
val PROTEIN = BasicProtein("pro", GENE)
val REGULATIVE = BasicRegulativeMolecule("reg")
val DETERIORATING_REGULATIVE = BasicDeterioratingMolecule("det reg")
val REGULATOR = BasicRegulator(REGULATIVE, PROTEIN)

private const val DELTA = 10e-15

infix fun<T : Number> Collection<T>.shouldMatchWithTolerance(expected: Collection<T>) {
    this shouldHaveSize expected.size
    forEachIndexed { i, v ->
        if (abs(v.toDouble() - expected.elementAt(i).toDouble()) > DELTA)
            fail("Collection should have value ${expected.elementAt(i)} in position $i but it has $v")
    }
}

infix fun<T : Number> Sequence<T>.shouldMatchWithTolerance(expected: Sequence<T>) =
    toList().shouldMatchWithTolerance(expected.toList())

infix fun<T> T.shouldEqual(expected: Any?) {
    if (this != expected) fail("$this and $expected are not the same but they should be")
}

infix fun<T> T.shouldNotEqual(expected: Any?) {
    if (this == expected) fail("$this and $expected are the same but they shouldn't be")
}

val Int.elements : Matcher<Map<*, *>>
    get() = object : Matcher<Map<*, *>> {
        override fun test(value: Map<*, *>) =
            value.size.run {
                Result(
                    this == this@elements,
                    "map should have ${ this@elements} model.utils.elements but it has $this",
                    "map should not have ${ this@elements} model.utils.elements but it has"
                )
            }
    }