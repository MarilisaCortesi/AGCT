package model

import io.kotlintest.shouldBe
import io.kotlintest.shouldHave
import io.kotlintest.specs.StringSpec
import model.entities.*
import model.variables.Concentration

internal class TestEntities : StringSpec({
    "test basics" {
        MOLECULE testBasics "mol"
        DEGRADING testBasics "det"
        REGULATIVE testBasics "reg"
        DEGRADING_REGULATIVE testBasics "det reg"
        GENE testBasics "gen"
        MRNA testBasics "rna"
        PROTEIN testBasics "pro"
        REGULATED_GENE testBasics "[gen-reg]"
    }

    "test equality" {
        entity<Molecule>("mol") shouldEqual MOLECULE
        entity<Molecule>("mol") { initialConcentration = Concentration(2) } shouldEqual MOLECULE
        entity<Molecule>("oth") shouldNotEqual MOLECULE
        entity<Molecule>("pro") shouldNotEqual MOLECULE
    }
})

private infix fun BiochemicalEntity.testBasics(id: String) {
    this.id shouldBe id
    initialConcentration shouldBe Concentration()
    aliases shouldHave 0.elements
}