package sorting

import java.io.File

val WHITESPACE = Regex("\\s+")

fun String.splitToSequence(pattern: Regex): Sequence<String> = this.split(pattern).asSequence()
fun <T> Sequence<T>.repeat(): Sequence<T> = sequence {
    while (true)
        yieldAll(this@repeat)
}

fun Array<String>.parse(vararg defaults: Pair<String, String?>): Map<String, String?> {
    return defaults.asSequence()
        .plus(
            this.asSequence()
                .windowed(2, partialWindows = true)
                .filter { it[0].startsWith('-') }
                .map { it[0].substring(1) to if (it.getOrElse(1) { "-" }.startsWith('-')) null else it[1] }
        ).toMap()
}

fun main(args: Array<String>) {
    val defaults = arrayOf("dataType", "sortingType", "inputFile", "outputFile")
    val defaultValues = arrayOf("word", "natural", null, null)
    val argsMap = args.parse(*(defaults.zip(defaultValues)).toTypedArray())

    for ((arg, _) in argsMap) {
        if (arg !in defaults) {
            println("\"$arg\" is not a valid parameter. It will be skipped.")
        }
    }

    val input = when (val inputFile = argsMap["inputFile"]) {
        null -> System.`in`
        else -> File(inputFile).inputStream()
    }

    val (values, arg) = when (argsMap["dataType"]) {
        "word" -> input.readWords() to "words"
        "line" -> input.readLines() to "lines"
        "long" -> input.readInts() to "numbers"
        else -> return println("No data type defined!")
    }

    val output = when (val outputFile = argsMap["outputFile"]) {
        null -> System.out
        else -> {
            val file = File(outputFile)
            file.createNewFile()
            file.outputStream()
        }
    }

    // output.bufferedWriter().use {}    For some reason closing the output stream causes the tests to fail. So much for memory safety.
    val it = output.bufferedWriter()
    val total = values.values.sum()
    it.appendLine("Total $arg: $total.")

    when (argsMap["sortingType"]) {
        "natural" -> {
            val sorted = values.asSequence()
                .flatMap { (value, freq) -> sequenceOf(value).repeat().take(freq) }
                .joinToString(" ")

            it.appendLine("Sorted data: $sorted\n")
        }
        "byCount" -> {
            val sorted = values.map { it.value to it.key }
                .sortedWith(compareBy({ it.first }, { it.second }))
                .joinToString("\n") { "${if (arg == "lines") (it.second as String).substring(1) else it.second}: ${it.first} time(s), ${it.first * 100 / total}%" }

            it.appendLine(sorted)
        }
        else -> return println("No sorting type defined!")
    }

    it.flush()
}