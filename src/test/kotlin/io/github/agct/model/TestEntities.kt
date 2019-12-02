package io.github.agct.model

import io.github.agct.DEGRADING
import io.github.agct.DEGRADING_REGULATIVE
import io.github.agct.ENTITY
import io.github.agct.GENE
import io.github.agct.MRNA
import io.github.agct.PROTEIN
import io.github.agct.REGULATED_GENE
import io.github.agct.REGULATED_MRNA
import io.github.agct.REGULATIVE
import io.github.agct.elements
import io.kotlintest.shouldBe
import io.kotlintest.shouldHave
import io.kotlintest.specs.StringSpec
import io.github.agct.model.entities.Entity
import io.github.agct.model.entities.entity
import io.github.agct.model.variables.BasicConcentration
import io.github.agct.shouldEqual
import io.github.agct.shouldNotEqual

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
        entity<Entity>("ent") {
            initialConcentration = BasicConcentration(2)
        } shouldEqual ENTITY
        entity<Entity>("oth") shouldNotEqual ENTITY
        entity<Entity>("pro") shouldNotEqual ENTITY
    }
})

private infix fun Entity.testBasics(id: String) {
    this.id shouldBe id
    initialConcentration shouldBe BasicConcentration()
    aliases shouldHave 0.elements
}