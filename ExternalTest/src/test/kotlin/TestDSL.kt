import dsl.*

fun main() {
    Fill circuit "My Circuit" with {
        the gene "gen" that codes For {
            the protein "pro" being regulated by {
                the molecule "reg"
            }
        }
    } having {
        default initial concentration into logspace(0, 4, 5)
        default degradation rate into range(1, 9, 2)
    } then export to entities and reactions and AGCT // and Alchemist
}