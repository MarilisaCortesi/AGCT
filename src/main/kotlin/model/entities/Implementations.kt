package model.entities

import model.utils.checkEquals
import model.utils.className

internal abstract class AbstractEntity(parameters: EntityParameters) : BiochemicalEntity {
    override val id = parameters.id
    override val initialConcentration = parameters.initialConcentration
    override val aliases = parameters.aliases.toList()

    override fun toString() = "$className(\"$id\")"

    override fun hashCode() =
        id.hashCode()

    override fun equals(other: Any?) =
        checkEquals(other) { id == it.id }
}

internal abstract class AbstractBoundEntity<out F : BiochemicalEntity, out S : BiochemicalEntity>(
    override val first: F,
    override val second: S
) : AbstractEntity(EntityParameters().apply { id = "[${first.id}-${second.id}]" }), BoundBiochemicalEntity<F, S>

internal class BasicMolecule(parameters: EntityParameters) :
    AbstractEntity(parameters), Molecule

internal class BasicDegradingMolecule(parameters: EntityParameters) :
    AbstractEntity(parameters), DegradingMolecule

internal class BasicRegulatingMolecule(parameters: EntityParameters) :
    AbstractEntity(parameters), RegulatingMolecule

internal class BasicDegradingRegulatingMolecule(parameters: EntityParameters) :
    AbstractEntity(parameters), DegradingRegulatingMolecule

internal class BasicGene(parameters: EntityParameters) :
    AbstractEntity(parameters), Gene

internal class BasicMRna(parameters: EntityParameters) :
    AbstractEntity(parameters), MRna

internal class BasicProtein(parameters: EntityParameters) :
    AbstractEntity(parameters), Protein

internal class RegulatedGene(gene: Gene, regulator: RegulatingMolecule) :
    AbstractBoundEntity<Gene, RegulatingMolecule>(gene, regulator), Gene