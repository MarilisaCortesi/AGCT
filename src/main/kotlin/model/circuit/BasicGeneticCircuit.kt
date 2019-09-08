package model.circuit

import model.entities.*
import model.reactions.*
import model.reactions.BiochemicalReaction
import model.reactions.CodingReaction
import model.reactions.Degradation
import model.reactions.Transcription
import model.reactions.Translation
import model.utils.toConsole
import java.lang.IllegalStateException

/**
 * A genetic circuit with basic limitations:
 * 0. the same reaction cannot be set twice for the same entity;
 * 1. a degrading molecule must have one and one degrading reaction only;
 * 2. a gene can code directly for an arbitrary number of proteins, but a protein can be coded by one gene only;
 * 3. a gene can code for an arbitrary number of mRNA molecules, but a mRNA molecule can be coded by one gene only;
 * 4. a molecule of mRNA can code for one protein only, as well as one protein can be coded by one molecule of mRNA only;
 * 5. a regulator can regulate an arbitrary number of coding reactions;
 * 6. a coding reaction can have an arbitrary number of regulators;
 *
 * For any of them that is broken, an exception will be thrown.
 */
internal class BasicGeneticCircuit(override val name: String) : GeneticCircuit({
    exportRules["one degradation reaction at least"] = { circuit ->
        circuit.filterKeys { it is DegradingEntity }
            .filterValues { it.filterIsInstance<Degradation>().isEmpty() }
            .keys
            .firstOrNull()
            ?.let { entity ->
                throw IllegalStateException("Degradation reaction not set for $entity")
            }
    }

    // includes "one degradation reaction at most" as a degradation reaction is made of the entity only
    addingRules["no duplicate reactions"] = { set, entity, reaction ->
        require(!set.contains(reaction)) { "$reaction already set for $entity." }
    }

    addingRules["one protein transcription/translation at most"] = { set, entity, reaction ->
        if (entity is Protein && reaction is CodingReaction<*, *>) {
            set.filterIsInstance<CodingReaction<*, *>>().firstOrNull()?.run {
                throw IllegalArgumentException("$entity is already transcribed/translated by $coder")
            }
        }
    }

    addingRules["one mRNA transcription at most"] = { set, entity, reaction ->
        if (entity is MRna && reaction is Transcription<*>) {
            set.filterIsInstance<Transcription<*>>().firstOrNull()?.run {
                throw IllegalArgumentException("$entity is already transcribed by $coder")
            }
        }
    }

    addingRules["one mRNA translation at most"] = { set, entity, reaction ->
        if (entity is MRna && reaction is Translation) {
            set.filterIsInstance<Translation>().firstOrNull()?.run {
                throw IllegalArgumentException("$entity already translates $target")
            }
        }
    }
})