package io.github.agct.examples

import agct.*
import io.github.agct.dsl.keywords.basal
import io.github.agct.dsl.keywords.codes
import io.github.agct.dsl.keywords.degradation
import io.github.agct.dsl.keywords.export
import io.github.agct.dsl.keywords.initial
import io.github.agct.dsl.keywords.linspace
import io.github.agct.dsl.keywords.logspace
import io.github.agct.dsl.keywords.reaction
import io.github.agct.dsl.keywords.regulating
import io.github.agct.dsl.levels.Create

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