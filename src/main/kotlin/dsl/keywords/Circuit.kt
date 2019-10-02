@file:Suppress("PackageDirectoryMismatch", "ClassName")

package agct

object codes {
    operator fun invoke(block: TranscriptionLevel.() -> Unit) = Block(block)
    class Block internal constructor(val block: TranscriptionLevel.() -> Unit)
}