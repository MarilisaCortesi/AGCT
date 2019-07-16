package dsl.keywords

import dsl.circuit.CircuitContext
import dsl.keywords.utils.FirstTimeWrapper
import dsl.keywords.utils.KeywordWrapper
import dsl.keywords.utils.SecondTimeWrapper
import dsl.keywords.utils.WrappableKeyword
import model.circuit.exports.exportToAGCT

// KEYWORD
@Suppress("ClassName")
object export : WrappableKeyword<CircuitContext, ExportWrapper>({ FirstTimeExportWrapper(it) })

// WRAPPERS
interface ExportWrapper : KeywordWrapper {
    infix fun to(type: ExportType) : SecondTimeExportWrapper
}

class FirstTimeExportWrapper internal constructor(private val context: CircuitContext) :
    FirstTimeWrapper<ExportType>(), ExportWrapper {
    override fun performOn(obj: ExportType) {
        when (obj) {
            AGCT -> context.exportToAGCT()
            Alchemist -> throw NotImplementedError("Alchemist export not yet supported.")
            else -> throw IllegalArgumentException("Unknown export type.")
        }
    }

    override infix fun to(type: ExportType) =
        SecondTimeExportWrapper(this).apply { performOn(type) }

    infix fun to(types: Array<ExportType>) =
        SecondTimeExportWrapper(this).apply { types.forEach { performOn(it) } }
}

class SecondTimeExportWrapper internal constructor(wrapped: FirstTimeExportWrapper) :
    SecondTimeWrapper<ExportType, SecondTimeExportWrapper>(wrapped), ExportWrapper by wrapped