import agct.*

fun main() {
    Create circuit "Genetic Toggle Switch" containing {
        the gene "promoter1" that codes {
            the protein "repressor2"
            regulated by {
                the protein "repressor1"
            } and {
                the molecule "inducer1"
            }
        }

        the gene "promoter2" that codes {
            the protein "repressor1"
            regulated by {
                the protein "repressor2"
            } and {
                the molecule "inducer2"
            }
        }

        "promoter1" {
            has an initial.concentration of 1
        }

        "promoter2" {
            has an initial.concentration of 1
        }
    } with {
        a default initial.concentration of 0
        a default degradation.rate of 1
        a default basal.rate of 1
        a default regulating.rate of 1
        a default binding.rate of 1
        a default unbinding.rate of 1
    }
}