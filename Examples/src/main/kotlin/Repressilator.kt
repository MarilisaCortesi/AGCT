import agct.*
import generation.utils.Level.Companion.start
import utils.ExportableAlchemist

fun main() {
    Create circuit "Repressilator" containing {
        the gene "gTetR" that codes For {
            the protein "TetR"
            regulated by { the protein "LacI" }
        }

        the gene "gLacI" that codes For {
            the protein "LacI"
            regulated by { the protein "AcI" }
        }

        the gene "gAcI" that codes For {
            the protein "AcI"
            regulated by { the protein "TetR" }
        }
    } with {
        a default initial.concentration of 1
        a default degradation.rate of 0.1
        a default basal.rate of 0
        a default regulating.rate of 10
        a default binding.rate of 0.01
        a default unbinding.rate of 0.01
    } then export to ExportableAlchemist {
        start(prefix = null, postfix = null, indentation = "  ", stringSeparator = ": ") {
            "export:" {
                line("- time")
                "- molecule: TetR" {
                    "aggregators"("[mean]")
                }
                "- molecule: LacI" {
                    "aggregators"("[mean]")
                }
                "- molecule: AcI" {
                    "aggregators"("[mean]")
                }
            }
        }.toString()
    }
}