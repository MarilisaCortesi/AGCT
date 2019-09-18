@file:Suppress("PackageDirectoryMismatch", "ClassName")

package agct

object codes {
    operator fun invoke(block: TranscriptionLevel.() -> Unit) = block
}