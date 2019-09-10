package generation.utils

import java.io.File

fun String.toFile(filename: String, directory: String) =
    "export${File.separator}${directory.toLowerCase()}${File.separator}".let { path ->
        File(path).mkdirs()
        File(path + filename).writeText(this)
    }