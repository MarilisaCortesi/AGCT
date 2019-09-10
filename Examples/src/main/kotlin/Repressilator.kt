import dsl.*
import generation.objects.*

fun main() {
    Create circuit "Repressilator" containing {
        the gene "g/TetR" that {
            codes For {
                the protein "TetR"
                regulated by { the protein "LacI" }
            }
        }

        the gene "g/LacI" that {
            codes For {
                the protein "LacI"
                regulated by { the protein "λcI" }
            }
        }

        the gene "g/λcI" that {
            codes For {
                the protein "λcI"
                regulated by {
                    the protein "TetR"
                    with a regulating.rate into linspace(1, 2, 3)
                }
            }
        }
    } then export to entities and reactions and AGCT and Alchemist
}