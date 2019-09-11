package generation.utils

open class Level private constructor(
    private val prefix: String?,
    private val postfix: String?,
    private val indentation: String,
    private val stringSeparator: String = ""
) {
    companion object {
        fun start(
            prefix: String? = "",
            postfix: String? = "",
            indentation: String = "\t",
            stringSeparator: String = "",
            block: Level.() -> Unit
        ) = Level(prefix, postfix, indentation, stringSeparator).apply(block)
    }

    private val lines = mutableListOf<Pair<String, Int>>()

    operator fun String.invoke(vararg appended: Any) =
        line(appended.joinToString(stringSeparator, "$this$stringSeparator"))

    operator fun String.invoke(
        pre: String? = prefix,
        post: String? = postfix,
        ind: String = indentation,
        sep: String = stringSeparator,
        block: Level.() -> Unit
    ) = level(this, pre, post, ind, sep, block)

    operator fun<T> Collection<T>.invoke(
        line: ((T) -> String)? = { "" },
        innerLine: ((T) -> String)? = line,
        spacings: Int = 0,
        pre: String? = prefix,
        post: String? = postfix,
        block: Level.(T) -> Unit
    ) {
        if (size == 0) return

        line?.invoke(elementAt(0))?.invoke(pre, post) { block(elementAt(0)) }
        drop(1).forEach {
            if (spacings < 0) {
                lines.removeAt(lines.lastIndex)
                "$post${innerLine?.invoke(it)}".invoke(pre, post) { block(it) }
            } else {
                repeat(spacings) { line() }
                innerLine?.invoke(it)?.invoke(pre, post) { block(it) }
            }
        }
    }

    operator fun<T> Collection<T>.invoke(
        line: String? = "",
        innerLine: String? = line,
        spacings: Int = 0,
        pre: String? = prefix,
        post: String? = postfix,
        block: Level.(T) -> Unit
    ) = run {
        val lineFunction: ((T) -> String)? = if (line == null) { null } else { { line } }
        val innerFunction: ((T) -> String)? = if (innerLine == null) { null } else { { innerLine } }
        invoke(lineFunction, innerFunction, spacings, pre, post, block)
    }

    fun line(line: String? = ""): Level {
        if (line != null)
            lines.add(line to 0)
        return this
    }

    fun level(
        line: String? = "",
        pre: String? = prefix,
        post: String? = postfix,
        ind: String = indentation,
        sep: String = stringSeparator,
        block: Level.() -> Unit
    ) = apply {
        line(line + prefix)
        with(lines) {
            addAll(start(pre, post, ind, sep, block).lines.map { it.first to it.second + 1 })
            if (size > 0 && elementAt(lastIndex).first == "") {
                removeAt(lastIndex)
            }
        }
        line(postfix)
    }

    override fun toString() =
        lines.joinToString("\n") { "${it.second.tabs}${it.first}" }

    private val Int.tabs
        get() = indentation.repeat(this)

    protected operator fun String?.plus(other: String?) =
        when {
            this == null -> other
            other == null -> this
            else -> "$this$other"
        }
}


