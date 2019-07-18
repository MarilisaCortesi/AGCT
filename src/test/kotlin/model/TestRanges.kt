package model

import io.kotlintest.specs.StringSpec
import model.variables.*
import kotlin.math.sqrt

private val FIRST_SEQUENCE_LIN = sequenceOf(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)
private val SECOND_SEQUENCE_LIN = sequenceOf(-1.0, -0.7, -0.4, -0.1, 0.2, 0.5, 0.8)
private val FIRST_SEQUENCE_LOG = sequenceOf(0.001, 0.01, 0.1, 1.0, 10.0, 100.0, 1000.0)
private val SECOND_SEQUENCE_LOG = sequenceOf(1.0, sqrt(10.0), 10.0, sqrt(1000.0), 100.0)

class TestRanges : StringSpec({
    "test values" {
        values(0, 1, 2, 3, 4, 5, 6, 7, 8, 9) shouldMatchWithTolerance FIRST_SEQUENCE_LIN
        values(-1, -0.7, -0.4, -0.1, 0.2, 0.5, 0.8) shouldMatchWithTolerance SECOND_SEQUENCE_LIN
    }

    "test range" {
        range(0, 9) shouldMatchWithTolerance FIRST_SEQUENCE_LIN
        range(-1, 1, 0.3) shouldMatchWithTolerance SECOND_SEQUENCE_LIN
    }

    "test linspace" {
        linspace(0, 9, 10) shouldMatchWithTolerance FIRST_SEQUENCE_LIN
        linspace(-1, 0.8, 7) shouldMatchWithTolerance SECOND_SEQUENCE_LIN
    }

    "test logspace" {
        logspace(-3, 3, 7) shouldMatchWithTolerance FIRST_SEQUENCE_LOG
        logspace(0, 2, 5) shouldMatchWithTolerance SECOND_SEQUENCE_LOG
    }

    "test geomspace" {
        geomspace(0.001, 1000, 7) shouldMatchWithTolerance FIRST_SEQUENCE_LOG
        geomspace(1, 100, 5) shouldMatchWithTolerance SECOND_SEQUENCE_LOG
    }
})