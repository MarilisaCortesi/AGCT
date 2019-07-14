package model.entities

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import model.utils.*
import model.variables.Concentration
import model.variables.Rate

private val DEFAULT_RATE = Rate(1, 2, 3, 4, 5)
private val DEFAULT_CONCENTRATION = Concentration(1, 2, 3, 4, 5)
private val<T : Molecule> T.TEST_NAME
    get() = "Very Very Long Named ${javaClass.simpleName.removePrefix("Basic")}"

private const val FIRST_ID = "first"
private const val SECOND_ID = "second"

class TestEntities : StringSpec({
    "test molecule" {
        MOLECULE.run {
            id shouldBe "mol"
            fullName shouldBe id
            initialConcentration shouldBe Concentration()
            fullName = TEST_NAME
            fullName shouldBe TEST_NAME
            initialConcentration = DEFAULT_CONCENTRATION
            initialConcentration shouldBe DEFAULT_CONCENTRATION
        }
    }

    "test deteriorating molecule" {
        DETERIORATING.run {
            id shouldBe "det"
            deteriorationRate shouldBe Rate()
            deteriorationRate = DEFAULT_RATE
            deteriorationRate shouldBe DEFAULT_RATE
        }
    }

    "test deteriorating and regulative molecule" {
        DETERIORATING_REGULATIVE.run {
            id shouldBe "det reg"
            deteriorationRate shouldBe Rate()
            deteriorationRate = DEFAULT_RATE
            deteriorationRate shouldBe DEFAULT_RATE
        }
    }

    "test protein" {
        PROTEIN.run {
            id shouldBe "pro"
            coder shouldBe GENE
            basalRate shouldBe Rate()
            basalRate = DEFAULT_RATE
            basalRate shouldBe DEFAULT_RATE
        }
    }

    "test regulator" {
        REGULATOR.run {
            id shouldBe "regulator\\reg"
            self shouldBe REGULATIVE
            target shouldBe PROTEIN
            unificationRate shouldBe Rate()
            separationRate shouldBe Rate()
            codingRate shouldBe Rate()
            unificationRate = DEFAULT_RATE
            separationRate = DEFAULT_RATE
            codingRate = DEFAULT_RATE
            unificationRate shouldBe DEFAULT_RATE
            separationRate shouldBe DEFAULT_RATE
            codingRate shouldBe DEFAULT_RATE
        }
    }

    "test equality" {
        val first = listOf(
            BasicMolecule(FIRST_ID),
            BasicDeterioratingMolecule(FIRST_ID),
            BasicRegulativeMolecule(FIRST_ID),
            BasicGene(FIRST_ID),
            BasicProtein(FIRST_ID, GENE)
        )
        val second = listOf(
            BasicMolecule(SECOND_ID),
            BasicDeterioratingMolecule(SECOND_ID),
            BasicRegulativeMolecule(SECOND_ID),
            BasicGene(SECOND_ID),
            BasicProtein(SECOND_ID, GENE)
        )
        for (molecule in first) {
            first.forEach { it shouldEqual molecule }
            second.forEach { it shouldNotEqual molecule }
        }
    }
})