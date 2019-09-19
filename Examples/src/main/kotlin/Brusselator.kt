import agct.*
import utils.ExportableAlchemist
import utils.line

fun main() {
    Create circuit "Brusselator" containing {
        the gene "A" that codes {
            the protein "X"
        }

        the gene "B" that codes {
            the protein "Y"
            with a basal.rate of 0
            regulated by {
                the protein "X"
                with a regulating.rate of infinite
            }
        }

        chemical reactions {
            "2X + Y" to "3X" having reaction.rate into values(1, 2, 4)
        }

        "A" {
            has an initial.concentration of 1
        }

        "B" {
            has an initial.concentration of 3
        }
    } with {
        a default initial.concentration of 0
        a default degradation.rate of 1
        a default basal.rate of 1
        a default binding.rate of 1
        a default unbinding.rate of 1
    } then export to ExportableAlchemist {
        line("- time")
        line("- molecule: A")
        line("  aggregators: [mean]")
        line("- molecule: B")
        line("  aggregators: [mean]")
    }
}