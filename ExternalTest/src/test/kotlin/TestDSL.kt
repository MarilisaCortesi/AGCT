import dsl.*

fun main() {
    Create circuit "My Circuit" containing {
        the gene "gen" that {
            has an initial_concentration of 20

            codes_for {
                the protein "pro" that {
                    has an initial_concentration of 50
                    has a degradation_rate of 2
                }

                with a basal_rate of 5

                being regulatedBy {
                    the molecule "reg" that {
                        has an initial_concentration of 200
                        has a degradation_rate of 5
                    }

                    with a regulated_rate of 10
                    with a binding_rate of 5
                    with an unbinding_rate of 5
                }
            }
        }
    } with {
        a default initial_concentration into logspace(0, 4, 5)
        a default degradation_rate into range(1, 9, 2)
    } then export to entities_list and reactions_list // and AGCT and Alchemist
}

/*
    Create circuit "My Circuit" containing {
        [the] (gene) -"gA"- (that) {
            [has] (an) [initialConcentration] (of) ...

            [codesFor] {
                [the] (protein) - "pA" - (that) {
                    [has] (an) [initialConcentration] (of) ...
                    [has] (a) [degradationRate] (of) ...
                }

                [with] (a) [basalRate] (in / of) ...

                [regulatedBy] {
                    [the] (molecule) "rA" that {
                        [has] (an) [initialConcentration] (of) ...
                        [has] (a) [degradationRate] (of) ...
                    }

                    [with] (a) [regulatedRate] (in / of) ...
                    [with] (a) [bindingRate] (in / of) ...
                    [with] (a) [unbindingRate] (in / of) ...
                }
            }
        }
    }


    Create circuit "My Circuit" containing {
        [the] (gene) -"gA"- (that) {
            [has] (an) [initialConcentration] (of/in) ...

            [codes] (for) [the] (protein) -"pA"- (that) {
                [has] ...
            } with the reaction {
                [having] (a) [basalRate] (in/of) ...

                [being] (regulated) [by] (the) (molecule) "rA" that {
                    [has] ...
                } with {
                    [a] (regulated) [rate] (in/of) ...
                    [a] (binding) [rate] (in/of) ...
                    [an] (unbinding) [rate] (in/of) ...
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