package dsl.contexts

import model.entities.BasicGene
import model.entities.Gene

class GeneContext(private val circuitContext: CircuitContext, id: String) {
    private val gene = circuitContext.getAndCheck<Gene>(id) ?: BasicGene(id)
}
