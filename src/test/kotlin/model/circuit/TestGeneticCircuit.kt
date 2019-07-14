package model.circuit

import model.utils.GENE
import model.utils.PROTEIN
import model.utils.REGULATOR
import model.utils.elements
import io.kotlintest.matchers.maps.shouldContainKey
import io.kotlintest.shouldHave
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import model.entities.BasicGene
import java.lang.IllegalStateException

class TestGeneticCircuit : StringSpec({
    with(BasicGeneticCircuit()) {
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
    }
})