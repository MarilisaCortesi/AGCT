@file:Suppress("PackageDirectoryMismatch", "ClassName")

package agct

import generation.AbstractGenerator

object reactions : ReactionsGenerator()

open class ReactionsGenerator : AbstractGenerator({ file ->
    file["export/${name.toLowerCase()}/reactions.txt"] =
        reactions.map { it.reactions }.flatten().joinToString("\n", "${name.toUpperCase()}\n\n")
})