import agct.*
import utils.ExportableAlchemist
import utils.line

fun main() {
    Create circuit "Brusselator" containing {
        the gene "A" that codes { the protein "X" }

        chemical reactions {
            "2X + Y" to "3X"
            "B + X" to "B + Y"
        }

        "A" {
            has an initial.concentration of 10
        }

        "B" {
            has an initial.concentration of 200
        }
    } with {
        a default initial.concentration of 0
        a default degradation.rate of 1
        a default basal.rate of 1
        a default reaction.rate of 1
    } then export to ExportableAlchemist {
        line("- time")
        line("- molecule: X")
        line("  aggregators: [mean]")
        line("- molecule: Y")
        line("  aggregators: [mean]")
    }
}