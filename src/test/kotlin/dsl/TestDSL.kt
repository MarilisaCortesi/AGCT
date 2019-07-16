package dsl

import dsl.keywords.*

fun main() {
    Create circuit "My Circuit" {

    } then export to AGCT

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