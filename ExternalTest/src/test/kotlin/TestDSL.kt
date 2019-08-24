import dsl.*

fun main() {
    Create circuit "My Circuit" containing {
        the gene "gen" that {
            codes For {
                the protein "pro"
                with a basal.rate of 5
                regulated by {
                    the molecule "reg"
                    with a regulating.rate of 10
                    with a binding.rate of 5
                    with an unbinding.rate of 5
                }
            }
        }

        "gen" {
            has an initial.concentration of 20
        }

        "reg" {
            has an initial.concentration of 200
            has a degradation.rate of 5
        }
    } with {
        a default initial.concentration into logspace(0, 4, 5)
        a default degradation.rate into range(1, 9, 2)
    } then export to all(entities, reactions, AGCT) // and Alchemist
}