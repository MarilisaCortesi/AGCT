import dsl.*
import generation.objects.*

fun main() {
    val a = Create circuit "Repressilator" containing {
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
                regulated by { the protein "TetR" }
            }
        }
    } then export to each one into (entities, reactions, AGCT, Alchemist)
}