package signature

import kotlin.io.path.Path
import kotlin.io.path.readLines

fun String.frameCenter(length: Int, border: String = ""): String {
    val (padStart, padEnd) = (length / 2 - this.length / 2).let {
        val (mod1, mod2) = length % 2 to this.length % 2

        if (mod1 == mod2) it to it
        else if (mod1 > mod2) it to it + 1
        else it - 1 to it
    }

    return "$border${" ".repeat(padStart)}${this}${" ".repeat(padEnd)}$border"
}

class Font(filename: String, spaceWidth: Int = 5) {
    val chars: Map<Char, List<String>>
    val height: Int

    init {
        val lines = Path(filename).readLines()
        height = lines[0].substringBefore(' ').toInt()
        val space = " ".repeat(spaceWidth)

        chars = lines.asSequence()
            .drop(1)
            .chunked(height + 1)
            .associate { l ->
                val char = l[0].substringBefore(' ')[0]
                val text = l.subList(1, l.size)
                char to text
            } + (' ' to List(height) { space })
    }

    fun textWidth(string: String): Int {
        return string.map { this.chars[it]!! }
            .sumOf { it[0].length }
    }

    fun applyTo(string: String, border: String = "", length: Int = string.length): String {
        val chars = string.map { this.chars[it]!! }

        return (0..chars[0].lastIndex)
            .joinToString("\n") { i ->
                chars.joinToString("") {
                    it[i]
                }.frameCenter(length, border)
            }
    }
}