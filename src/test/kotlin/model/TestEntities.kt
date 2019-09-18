package model

import io.kotlintest.shouldBe
import io.kotlintest.shouldHave
import io.kotlintest.specs.StringSpec
import model.entities.Entity
import model.entities.entity
import model.variables.BasicConcentration

internal class TestEntities : StringSpec({
    "test basics" {
        ENTITY testBasics "ent"
        DEGRADING testBasics "det"
        REGULATIVE testBasics "reg"
        DEGRADING_REGULATIVE testBasics "det reg"
        GENE testBasics "gen"
        MRNA testBasics "rna"
        PROTEIN testBasics "pro"
        REGULATED_GENE testBasics "gen_reg"
        REGULATED_MRNA testBasics "rna_reg"
    }

    "test equality" {
        entity<Entity>("ent") shouldEqual ENTITY
        entity<Entity>("ent") { initialConcentration = BasicConcentration(2) } shouldEqual ENTITY
        entity<Entity>("oth") shouldNotEqual ENTITY
        entity<Entity>("pro") shouldNotEqual ENTITY
    }
})

private infix fun Entity.testBasics(id: String) {
    this.id shouldBe id
    initialConcentration shouldBe BasicConcentration()
    aliases shouldHave 0.elements
}