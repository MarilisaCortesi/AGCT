package dsl.contexts

import model.entities.BasicGene
import model.entities.Gene
import model.entities.entity

/*
class GeneContext(private val circuitContext: CircuitContext, id: String) {
    private val gene = circuitContext.getAndCheck<Gene>(id) ?: entity<BasicGene>(id)
}
*/