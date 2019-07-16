/*
package dsl.gene

fun gene(id: String) = GeneContext(id)

fun gene(id: String, block: GeneContext.() -> Unit) = gene(id) then block

infix fun GeneContext.then(block: GeneContext.() -> Unit) =
    performOn {
        circuitContext.put(gene)
        block()
    }
*/
