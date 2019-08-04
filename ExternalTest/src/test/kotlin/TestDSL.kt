import dsl.*

fun main() {
    Create circuit "My Circuit" containing {
        the gene "gA"

        the protein "pA"

        the molecule "rA"

        the molecule "rB"

        "gA" {
            has an initialConcentration of 20
        }

        "pA" {
            has an initialConcentration of 50
        }

        "rA" {
            has a degradationRate of 2
        }
    } with {
        a default initialConcentration into logspace(0, 4, 5)
        a default degradationRate into range(1, 9, 2)
    } then export to AGCT // and Alchemist
}

/*
    Create circuit "My Circuit" containing {
        The gene "gA" that {
            [has] (an) [initialConcentration] (of/in) ...

            [codes] (for) [the] (protein) "pA" (that) {
                [has] (an) [initialConcentration] (of/in) ...
                [has] (a) [degradationRate] (of/in) ...
            } where the transcription {
                [has] (a) [basalRate] (in/of) ...

                [is] (regulatedBy) [the] (molecule) "rA" that {
                    [has] (an) [initialConcentration] (of/in) ...
                    [has] (a) [degradationRate] (of/in) ...
                } where the regulation {
                    [has] (a) [regulatedRate] (in/of) ...
                    [has] (a) [bindingRate] (in/of) ...
                    [has] (an) [unbindingRate] (in/of) ...
                }


            }
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