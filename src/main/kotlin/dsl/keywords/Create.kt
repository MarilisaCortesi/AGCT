package dsl.keywords

import dsl.circuit.CircuitContext

val Create = create

@Suppress("ClassName")
object create {
    infix fun circuit(context: CircuitContext) = context
}
