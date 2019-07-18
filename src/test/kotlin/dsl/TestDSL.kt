package dsl

import dsl.contexts.CircuitContext
import dsl.keywords.*
import dsl.keywords.enums.AGCT
import dsl.keywords.utils.invoke
import model.entities.BasicGene
import model.entities.Gene

fun main() {
    val a = Create circuit "My Circuit" {
        Given gene "g"
    } then export to AGCT

    /*
        Create circuit "My Circuit" containing {
            the gene "gene" that {
                has an initialConcentration of/in Concentration()
            } and codes for {
                the protein "protein" that {
                    has an initialConcentration of/in Concentration()
                    has a deteriorationRate of/in Rate()
                } having {
                    a basalRate of/in Rate()
                    as reaction regulators {
                        the molecule "molecule" that {
                            has an initialConcentration of/in Concentration()
                            has a deteriorationRate of/in Rate()
                        } with {
                            a unificationRate of/in Rate()
                            a separationRate of/in Rate()
                            a codingRate of/in Rate()
                        }
                }
            }
        }

        Create circuit "c" containing {
            the gene "g1" that codes for {
                the protein "p11" having {
                    a basalRate of/in Rate()
                    as gene regulators {
                        the molecule "m111" with {
                            a unificationRate of/in Rate()
                            a separationRate of/in Rate()
                            a codingRate of/in Rate()
                        }
                        the molecule "m112" with {
                            a unificationRate of/in Rate()
                            a separationRate of/in Rate()
                            a codingRate of/in Rate()
                        }
                    }
                }
                the protein "p12" {
                    ...
                }
            }
            the gene "g2" {
                ...
            }
        }
     */

    /*
        Given a gene("g")

        val gA = gene("gA") {
            Which has an initial concentration of/in Concentration()
        }
        val pA = protein("pA")

        Create circuit "My Circuit" {
            Given a gene("g") {
                It has an initial concentration of/in Concentration()
                It codes for the protein("p") {
                    That has an initial concentration of/in Concentration()
                    That degradates with a rate of/in Rate()
                }
            }
        }
     */

    /*
    GENE [then] {
        It/That -has- an -initial- concentration -of/in- Concentration()
        It/That -codes- for -a- protein("P")
    }
    GENE -with- an ... -then-
    GENE -then- it -has- ... -and- it -codes- ...

    Given a gene("G") then it has an initial concentration of ...
    Given a gene("G") then it codes for a protein("P")
    Given a gene("G") then it has an initial concentration of ... and it codes for a protein("P")
    Given a gene("G") [then] {
        It has an initial concentration of ...
        It codes for a protein("P")
    } [and it has an initial concentration of ...]
    */

    /*
    PROTEIN {
        It/That -has- ...
        It/That -is- coded -with- a -basal- rate -of/in- Rate()
        It/That -degradates- with -a- rate -of/in- Rate()
        It/That -is- regulated -by- regulator("R")
    }
    PROTEIN -with- ...
    PROTEIN -then- it -has- ... -and- it -is- ...

    Taken the protein("P") then it has an initial concentration of ...
    Taken the protein("P") with a basal rate of ... and it is regulated by ...
    protein("P") {
        That has an initial concentration
    }
    */

    /*
    REGULATOR -with- {
        A -unification- rate -of- Rate()
        A -separation- rate -of- Rate()
        A -regulated- rate -of- Rate()
    }
    REGULATOR -having- a -unification- ... -and- a -separation- ... -and- a -regulated- ...
    */

    /*
    Given a gene("G") {
        It codes for a protein("P") {

        }
    }


    Given a gene("G") {
        It has an initial concentration of 100.0
        It codes for a protein("P") having {
            An initial concentration of 100.0
        } whose reaction has {
            A basal rate of 10.0
            A/The regulator "R" with {
                A unification rate of 1.0
                A separation rate of 1.0
                A coding rate of 1.0
            }
        }
    }

    Given a gene("G") then it codes for a protein("P") whose reaction has a basal rate of 2.0

    Given the gene("gA"). it has an initial concentration of 100.0 then {
        It codes for the protein("pA") having {
            A basal rate of 1.0
            As regulator the
        }
    }

    Given the gene("gen") {
        It codes for the protein("pro") with basal rate in (1.0, 2.0, 3.0) having {
            As regulator the molecule("reg") with {
                A unification rate of 1.0
                A separation rate of 1.0
                A coding rate of 1.0
            }
        }
    }

    Taken the gene("gen") it has an initial concentration of 100.0
    Taken the protein("pro") it has an initial concentration of 100.0 and a deterioration rate of 1.0
    Taken the molecule("reg") it has an initial concentration of 100.0
     */
}