import dsl.*

fun main() {
    Create circuit "My Circuit" containing {
        the gene "gen" that {
            has an initial_concentration of 20

            codes_for {
                the protein "pro" that {
                    has an initial_concentration of 50
                    has a degradation_rate of 2
                }

                with a basal_rate of 5

                regulatedBy {
                    the molecule "reg" that {
                        has an initial_concentration of 200
                        has a degradation_rate of 5
                    }

                    with a regulated_rate of 10
                    with a binding_rate of 5
                    with an unbinding_rate of 5
                }
            }
        }
    } with {
        a default initial_concentration into logspace(0, 4, 5)
        a default degradation_rate into range(1, 9, 2)
    } then export to entities_list and reactions_list and AGCT // and Alchemist
}