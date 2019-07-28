package dsl.keywords
/*

import dsl.contexts.CircuitContext
import dsl.keywords.enums.AGCT
import dsl.keywords.enums.Alchemist
import dsl.keywords.enums.ExportType
import dsl.keywords.utils.*

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
            AGCT -> throw NotImplementedError("AGCT export not yet supported.")
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
*/
