import agct.*

fun main() {
    Create circuit "Repressilator" containing {
        the gene "gTetR" that codes For {
            the protein "TetR"
            regulated by { the protein "LacI" }
        }

        the gene "gLacI" that codes For {
            the protein "LacI"
            regulated by { the protein "λcI" }
        }

        the gene "gλcI" that codes For {
            the protein "λcI"
            regulated by { the protein "TetR" }
        }
    } with {
        a default initial.concentration of 1
        a default degradation.rate of 0.1
        a default basal.rate into linspace( 0, 10, 3)
        a default regulating.rate into values(3, 4, 12)
        a default binding.rate of 0.01
        a default unbinding.rate of 0.01
    } then export to each one into (entities, reactions, AGCT, Alchemist)
}