package model.circuit.export

import model.circuit.GeneticCircuit
import model.circuit.export.Context.Companion.context
import model.entities.BiochemicalEntity
import model.entities.DegradingEntity
import model.entities.Gene
import model.entities.Protein
import model.entities.RegulatedGene
import model.entities.RegulatingEntity
import model.reactions.Degradation
import model.reactions.Regulation
import model.reactions.Transcription
import model.utils.UnsupportedClassException
import model.utils.toConsole
import model.variables.Variable
import java.lang.StringBuilder

internal class Context private constructor(val circuit: GeneticCircuit, val beforeBlock: String, val afterBlock: String) {
    private val builder: StringBuilder = StringBuilder()
    private var indentation = 0

    companion object {
        private var info: Context? = null

        val context
            get() = info ?: throw IllegalStateException("No contexts running.")

        fun start(circuit: GeneticCircuit, beforeBlock: String, afterBlock: String, routine: () -> Unit) =
            Context(circuit, beforeBlock, afterBlock).apply {
                if (info == null) {
                    info = this
                    routine()
                    info = null
                } else {
                    throw IllegalStateException("Another context is already running.")
                }
            }.builder.toString()

        fun line(line: String = "") {
            context.run {
                if (line == "") {
                    builder.append("\n")
                } else {
                    builder.append("${indentation.tabs}$line\n")
                }
            }
        }

        fun block(prefix: String, inner: () -> Unit) {
            context.run {
                line("$prefix$beforeBlock")
                indentation++
                inner()
                indentation--
                line(afterBlock)
            }
        }

        private val Int.tabs
            get() = "\t".repeat(this)
    }
}

internal fun GeneticCircuit.start(beforeBlock: String, afterBlock: String, routine: () -> Unit) =
    Context.start(this, beforeBlock, afterBlock, routine)

internal fun line(line: String = "") =
    Context.line(line)

internal fun block(prefix: String, inner: () -> Unit) =
    Context.block(prefix, inner)

internal fun <T> T.block(prefix: T.() -> String, inner: T.() -> Unit) {
    block(prefix(this)) { inner(this) }
}

internal fun <T> Collection<T>.blocks(prefix: T.() -> String, inner: T.() -> Unit) {
    forEachIndexed { index, element ->
        if (index != 0) line()
        element.block(prefix, inner)
    }
}