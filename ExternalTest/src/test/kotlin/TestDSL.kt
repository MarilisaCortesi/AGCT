import dsl.*

fun main() {
    Create circuit "Dr. Strepressilator, or: the Strange Repressilator" containing {
        the gene "g/TetR" that {
            codes For {
                the protein "TetR"
                regulated by { the protein "LacI" } and { the protein "λcI" }
            }
        }

        the gene "g/LacI" that {
            codes For {
                the protein "LacI"
                regulated by { the protein "λcI" } and { the protein "TetR" }
            }
        }

        the gene "g/λcI" that {
            codes For {
                the protein "λcI"
                regulated by { the protein "TetR" } and { the protein "LacI" }
            }
        }
    } then export to all(entities, reactions, AGCT) // and Alchemist
}