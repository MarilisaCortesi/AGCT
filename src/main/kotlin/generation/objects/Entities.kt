@file:Suppress("PackageDirectoryMismatch", "ClassName")

package agct

import generation.AbstractGenerator

object entities : EntitiesGenerator()

open class EntitiesGenerator : AbstractGenerator( { file ->
    file["export/${name.toLowerCase()}/entities.txt"] =
        entities.joinToString("\n", "${name.toUpperCase()}\n\n")
})