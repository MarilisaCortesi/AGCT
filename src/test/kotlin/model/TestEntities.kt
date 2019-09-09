package model

import io.kotlintest.shouldBe
import io.kotlintest.shouldHave
import io.kotlintest.specs.StringSpec
import model.entities.*
import model.variables.Concentration

internal class TestEntities : StringSpec({
    "test basics" {
        ENTITY testBasics "ent"
        DEGRADING testBasics "det"
        REGULATIVE testBasics "reg"
        DEGRADING_REGULATIVE testBasics "det reg"
        GENE testBasics "gen"
        MRNA testBasics "rna"
        PROTEIN testBasics "pro"
        REGULATED_GENE testBasics "gen-reg"
        REGULATED_MRNA testBasics "rna-reg"
    }

    "test equality" {
        entity<GeneticEntity>("ent") shouldEqual ENTITY
        entity<GeneticEntity>("ent") { initialConcentration = Concentration(2) } shouldEqual ENTITY
        entity<GeneticEntity>("oth") shouldNotEqual ENTITY
        entity<GeneticEntity>("pro") shouldNotEqual ENTITY
    }
})

private infix fun GeneticEntity.testBasics(id: String) {
    this.id shouldBe id
    initialConcentration shouldBe Concentration()
    aliases shouldHave 0.elements
}