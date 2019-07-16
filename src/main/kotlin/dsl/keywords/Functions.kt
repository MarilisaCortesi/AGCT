package dsl.keywords

import dsl.circuit.CircuitContext

/**
 * Creates a circuit context from a string.
 *
 * @receiver the name of the circuit
 * @param fill the circuit body
 */
operator fun String.invoke(fill: CircuitContext.() -> Unit) =
    CircuitContext(this).apply(fill)

/**
 * Create an array of export types.
 *
 * @param types the type of exports.
 */
fun all(vararg types: ExportType) = types