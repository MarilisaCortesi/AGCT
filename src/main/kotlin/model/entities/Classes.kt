package model.entities

import model.variables.Concentration
import model.variables.Rate

private val Regulator<*>.REGULATOR_ID
    get() = "regulator\\${self.id}"

/**
 * An abstract molecule with default values.
 */
abstract class AbstractMolecule(fullName: String) :  Molecule {
    override var fullName = fullName
    override var initialConcentration = Concentration()

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?) =
        if (this === other) true
        else {
            when (other) {
                is Molecule -> id == other.id
                else -> false
            }
        }
}

/**
 * An abstract deteriorating molecule with default values.
 */
abstract class AbstractDeterioratingMolecule(fullName: String) : AbstractMolecule(fullName), DeterioratingMolecule {
    override var deteriorationRate = Rate()
}

/**
 * A generic molecule with default values.
 */
class BasicMolecule internal constructor(override val id: String) : AbstractMolecule(id)

/**
 * A generic gene with default values.
 */
class BasicGene internal constructor(override val id: String) : AbstractMolecule(id), Gene

/**
 * A generic regulative molecule.
 */
class BasicRegulativeMolecule internal constructor(override val id: String) : AbstractMolecule(id), RegulativeMolecule

/**
 * A generic deteriorating molecule with default values.
 */
class BasicDeterioratingMolecule internal constructor(override val id: String) : AbstractDeterioratingMolecule(id)

/**
 * A generic regulative molecule with a deterioration rate.
 */
class BasicDeterioratingRegulativeMolecule internal constructor(override val id: String) :
    AbstractDeterioratingMolecule(id), RegulativeMolecule

/**
 * A generic protein with default values.
 */
class BasicProtein internal constructor(override val id: String, override val coder: Gene) :
    AbstractDeterioratingMolecule(id), Protein {
    override var basalRate = Rate()
}

/**
 * A generic regulator with default values.
 */
class BasicRegulator<R : RegulativeMolecule> internal constructor(override val self: R, override val target: Protein) :
    AbstractMolecule(self.id), Regulator<R> {
    override val id = REGULATOR_ID
    override var unificationRate = Rate()
    override var separationRate = Rate()
    override var codingRate = Rate()
}
