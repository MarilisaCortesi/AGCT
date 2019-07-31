package dsl

import dsl.levels.Create
import dsl.levels.degradationRate
import dsl.levels.export
import dsl.levels.initialConcentration
import dsl.utils.AGCT
import dsl.utils.values

fun main() {
    Create circuit "My Circuit" containing {
        the gene "gA" that {
            has an initialConcentration into values(1, 2, 3, 4, 5)
        }

        the gene "gB"

        the protein "pA" that {
            has an initialConcentration of 2
            has a degradationRate of 3
        }

        the regulator "rA"
    } then export to AGCT // and Alchemist
}

/*
    Create circuit "My Circuit" containing {
        The gene "gA" that {
            [has] (an) [initialConcentration] (of/in) ...

            [codes] (for) {
                [the] (protein) that {
                    [has] (an) [initialConcentration] (of/in) ...
                    [has] (a) [degradationRate] (of/in) ...
                }

                [with] (a) [transcriptionRate] (in/of) ...

                [regulated] by {
                    [the] (regulator) ...
                    [with] (a) [regulatedRate] (in/of) ...
                    [with] (a) [bindingRate] (in/of) ...
                    [with] (an) [unbindingRate] (in/of) ...
                }

                regulated by {
                    the regulator ...
                }
            }

            codes for {
                the protein ...
            }
        }
 */

/*
    () -> function
    [] -> keyword
    -- -> object

    [The] (geneticCircuit) -"My Circuit"-  (contains) {
        [the] (gene) -"gA"- (that) [codes] (for) {
            [the] (protein) -"pA"- (with) [a] (basal) [rate] (of/in) -Rate()- (being) [regulated] (by) {
                [the] (regulator) -"rA"- (with) {
                    [a] (coding) [rate] (of/in) -Rate()-
                    [a] (binding) [rate] (of/in) -Rate()-
                    [an] (unbinding) [rate] (of/in) -Rate()-
                }
            }
        }

        [the] (gene) -"gB"- (has) {
            [an] (initial) [concentration] (of/in) -Concentration()-
        }

        [the] (protein) -"pA"- (has) {
            [an] (initial) [concentration] (of/in) -Concentration()-
            [a] (deterioration) [rate] (of/in) -Rate()-
        }

        [the] (regulator) -"rA"- (has) {
            [an] (initial) [concentration] (of/in) -Concentration()-
        }
    }
 */

/*
    The geneticCircuit "My Circuit" contains {
        the gene "gA" thatCodesFor {
            the protein "pA" withABasalRateOf/In Rate() beingRegulatedBy {
                the regulator "rA" with {
                    a codingRateOf/In Rate()
                    a bindingRateOf/In Rate()
                    an unbindingRateOf/In Rate()
                }
            }
        } -> Unit

        the gene "gB" has/having {
            an initialConcentrationOf/In Concentration()
        } -> [andCodesFor??]

        the protein "pA" has/having {
            a initialConcentrationOf/In Concentration()
            a deteriorationRateOf/In Rate()
        } -> [withABasalRateIn/Of?? - havingAsRegulators??]

        the regulator "rA" has/having {
            an initialConcentrationOf/In Concentration()
        } -> [with??]

        ------------------------------------------------

        the gene "gB" has/having {
            an initialConcentrationOf/In Concentration()
        } andCodesFor {
            the protein "pB" has/having {
                a initialConcentrationOf/In Concentration()
                a deteriorationRateOf/In Rate()
            } withABasalRateOf/In Rate() havingAsRegulators {
                the regulator "rA" has/having {
                    an initialConcentrationOf/In Concentration()
                } with {
                    a codingRateOf/In Rate()
                    a bindingRateOf/In Rate()
                    an unbindingRateOf/In Rate()
                }
            }
        }
    }
 */