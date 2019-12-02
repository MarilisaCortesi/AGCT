package io.github.agct.examples

import agct.*
import io.github.agct.generation.target.AGCT
import io.github.agct.generation.target.Alchemist

fun main() {
    Create circuit "My Circuit" containing {
        gene("g") that codes {
            the protein "p"
            with a basal.rate into logspace(1, 5, 5)
            regulated by {
                the molecule "m1" that { has a degradation.rate of 0 }
                with a regulating.rate into linspace(1, 10, 10)
            } and { molecule("m2") }
        }

        chemical reactions {
            "p + m2" to "p + m1" having reaction.rate of 0.1
        }

        "p" { has an initial.concentration of 0 }
    } with {
        a default initial.concentration of 1
        a default regulating.rate of 5
    } then export to AGCT and Alchemist
}