package model

import io.kotlintest.matchers.collections.shouldHaveSingleElement
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import model.variables.*
import java.lang.IllegalArgumentException

private const val SINGLE_INTEGER_ELEMENT : Int = 20
private val MULTIPLE_INTEGER_ELEMENTS : Set<Int> = setOf(1, 2, 3, 4, 5)
private const val SINGLE_DOUBLE_ELEMENT : Double = 20.0
private val MULTIPLE_DOUBLE_ELEMENTS : Set<Double> = setOf(1.0, 2.0, 3.0, 4.0, 5.0)

internal class TestVariables : StringSpec({
    "test rate" {
        val defaultRate = BasicRate()
        defaultRate.values shouldHaveSingleElement DEFAULT_RATE_VALUE
        val singleRate = BasicRate(SINGLE_DOUBLE_ELEMENT)
        singleRate.values shouldHaveSingleElement SINGLE_DOUBLE_ELEMENT
        val arrayRate = BasicRate(*MULTIPLE_DOUBLE_ELEMENTS.toDoubleArray())
        arrayRate.values shouldMatchWithTolerance MULTIPLE_DOUBLE_ELEMENTS
        val sequenceRate = BasicRate(MULTIPLE_DOUBLE_ELEMENTS.asSequence())
        sequenceRate.values shouldMatchWithTolerance MULTIPLE_DOUBLE_ELEMENTS
        val setRate = BasicRate(MULTIPLE_DOUBLE_ELEMENTS)
        setRate.values shouldMatchWithTolerance MULTIPLE_DOUBLE_ELEMENTS
    }

    "test concentration" {
        val defaultConcentration = BasicConcentration()
        defaultConcentration.values shouldHaveSingleElement DEFAULT_CONCENTRATION_VALUE
        val singleConcentration = BasicConcentration(SINGLE_INTEGER_ELEMENT)
        singleConcentration.values shouldHaveSingleElement SINGLE_INTEGER_ELEMENT
        val arrayConcentration = BasicConcentration(*MULTIPLE_INTEGER_ELEMENTS.toIntArray())
        arrayConcentration.values shouldMatchWithTolerance MULTIPLE_INTEGER_ELEMENTS
        val sequenceConcentration = BasicConcentration(MULTIPLE_INTEGER_ELEMENTS.asSequence())
        sequenceConcentration.values shouldMatchWithTolerance MULTIPLE_INTEGER_ELEMENTS
        val setConcentration = BasicConcentration(MULTIPLE_INTEGER_ELEMENTS)
        setConcentration.values shouldMatchWithTolerance MULTIPLE_INTEGER_ELEMENTS
    }

    "test equality" {
        BasicRate(MULTIPLE_DOUBLE_ELEMENTS) shouldEqual BasicRate(MULTIPLE_DOUBLE_ELEMENTS)
        BasicConcentration(MULTIPLE_INTEGER_ELEMENTS) shouldEqual BasicConcentration(MULTIPLE_INTEGER_ELEMENTS)
        BasicRate(MULTIPLE_DOUBLE_ELEMENTS) shouldNotEqual BasicConcentration(MULTIPLE_INTEGER_ELEMENTS)
    }

    "test exception" {
        shouldThrow<IllegalArgumentException> {
            BasicRate(-1.0)
        }.message shouldBe "Variable must have non-negative values only."

        shouldThrow<IllegalArgumentException> {
            BasicConcentration(3, 2, 1, 0, -1)
        }.message shouldBe "Variable must have non-negative values only."
    }
})
