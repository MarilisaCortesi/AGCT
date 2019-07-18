package model

import io.kotlintest.matchers.maps.shouldContainKey
import io.kotlintest.shouldHave
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import model.circuit.BasicGeneticCircuit
import model.entities.BasicGene
import java.lang.IllegalStateException

class TestGeneticCircuit : StringSpec({
    BasicGeneticCircuit("test circuit").run {
        addGene(GENE)
        addProtein(PROTEIN)
        addRegulator(REGULATOR)

        molecules shouldHave 3.elements
        molecules shouldContainKey "gen"
        molecules shouldContainKey "pro"
        molecules shouldContainKey "reg"

        shouldThrow<IllegalStateException> {
            addGene(BasicGene("pro"))
        }

        shouldThrow<IllegalStateException> {
            addGene(BasicGene("gen"))
        }
    }
})