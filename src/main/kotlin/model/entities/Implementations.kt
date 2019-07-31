package model.entities

import model.utils.checkEquals
import model.utils.className
import model.utils.string

internal abstract class AbstractEntity(parameters: EntityParameters) : BiochemicalEntity {
    override val id = parameters.id
    override val initialConcentration = parameters.initialConcentration
    override val aliases = parameters.aliases.toList()

    override fun toString() = "$className(${id.string})"

    override fun hashCode() =
        id.hashCode()

    override fun equals(other: Any?) =
        checkEquals(other) { id == it.id }
}

internal abstract class AbstractBoundEntity<out F : BiochemicalEntity, out S : BiochemicalEntity>(
    override val first: F,
    override val second: S
) : AbstractEntity(EntityParameters().apply { id = "[${first.id}-${second.id}]" }), BoundBiochemicalEntity<F, S>

internal class BasicDegradingEntity(parameters: EntityParameters) :
    AbstractEntity(parameters), DegradingEntity

internal class BasicRegulatingEntity(parameters: EntityParameters) :
    AbstractEntity(parameters), RegulatingEntity

internal class DegradingRegulatingMolecule(parameters: EntityParameters) :
    AbstractEntity(parameters), DegradingEntity, RegulatingEntity

internal class BasicEntity(parameters: EntityParameters) :
    AbstractEntity(parameters)

internal class BasicGene(parameters: EntityParameters) :
    AbstractEntity(parameters), Gene

internal class BasicMRna(parameters: EntityParameters) :
    AbstractEntity(parameters), MRna

internal class BasicProtein(parameters: EntityParameters) :
    AbstractEntity(parameters), Protein

internal class RegulatedMRna(mRna: MRna, regulator: RegulatingEntity) :
    AbstractBoundEntity<MRna, RegulatingEntity>(mRna, regulator), MRna

internal class RegulatedGene(gene: Gene, regulator: RegulatingEntity) :
    AbstractBoundEntity<Gene, RegulatingEntity>(gene, regulator), Gene