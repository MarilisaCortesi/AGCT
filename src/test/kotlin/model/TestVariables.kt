package model

import io.kotlintest.matchers.collections.shouldHaveSingleElement
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import model.variables.*
import java.lang.IllegalArgumentException

private const val SINGLE_ELEMENT = 20.0
private val MULTIPLE_ELEMENTS = setOf(1.0, 2.0, 3.0, 4.0, 5.0)

internal class TestVariables : StringSpec({
    "test rate" {
        val defaultRate = BasicRate()
        defaultRate.values shouldHaveSingleElement DEFAULT_RATE_VALUE
        val singleRate = BasicRate(SINGLE_ELEMENT)
        singleRate.values shouldHaveSingleElement SINGLE_ELEMENT
        val arrayRate = BasicRate(*MULTIPLE_ELEMENTS.toTypedArray())
        arrayRate.values shouldMatchWithTolerance MULTIPLE_ELEMENTS
        val sequenceRate = BasicRate(MULTIPLE_ELEMENTS.asSequence())
        sequenceRate.values shouldMatchWithTolerance MULTIPLE_ELEMENTS
        val setRate = BasicRate(MULTIPLE_ELEMENTS)
        setRate.values shouldMatchWithTolerance MULTIPLE_ELEMENTS
    }

    "test concentration" {
        val defaultConcentration = BasicConcentration()
        defaultConcentration.values shouldHaveSingleElement DEFAULT_CONCENTRATION_VALUE
        val singleConcentration = BasicConcentration(SINGLE_ELEMENT)
        singleConcentration.values shouldHaveSingleElement SINGLE_ELEMENT
        val arrayConcentration = BasicConcentration(*MULTIPLE_ELEMENTS.toTypedArray())
        arrayConcentration.values shouldMatchWithTolerance MULTIPLE_ELEMENTS
        val sequenceConcentration = BasicConcentration(MULTIPLE_ELEMENTS.asSequence())
        sequenceConcentration.values shouldMatchWithTolerance MULTIPLE_ELEMENTS
        val setConcentration = BasicConcentration(MULTIPLE_ELEMENTS)
        setConcentration.values shouldMatchWithTolerance MULTIPLE_ELEMENTS
    }

    "test equality" {
        BasicRate(MULTIPLE_ELEMENTS) shouldEqual BasicRate(MULTIPLE_ELEMENTS)
        BasicConcentration(MULTIPLE_ELEMENTS) shouldEqual BasicConcentration(MULTIPLE_ELEMENTS)
        BasicRate(MULTIPLE_ELEMENTS) shouldNotEqual BasicConcentration(MULTIPLE_ELEMENTS)
    }

    "test exception" {
        shouldThrow<IllegalArgumentException> {
            BasicRate(-1)
        }.message shouldBe "Variable must have non-negative values only."

        shouldThrow<IllegalArgumentException> {
            BasicConcentration(3, 2, 1, 0, -1)
        }.message shouldBe "Variable must have non-negative values only."
    }
})
