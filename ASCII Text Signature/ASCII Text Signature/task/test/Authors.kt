import java.util.*

private class Letter(
        val rows: List<String>
) {
    init {
        if (rows.distinctBy { it.length }.size != 1) {
            throw IllegalStateException("Bad letter. Not equal width in lines: ${rows.distinctBy { it.length }}")
        }
    }

    val width get() = rows[0].length
    val height get() = rows.size
}

private class Font(
        val charsToLetters: MutableMap<Char, Letter>,
        val height: Int
) {
    operator fun get(char: Char) = charsToLetters[char]
}

private fun makeLetters(fontStr: String): Font {
    val scanner = Scanner(fontStr)

    val h = scanner.nextInt()
    val n = scanner.nextInt()

    val charsToLetters = mutableMapOf<Char, Letter>()

    repeat(n) {
        val char = scanner.next()[0]
        val w = scanner.nextInt()
        scanner.nextLine()

        val rows = mutableListOf<String>()
        repeat(h) {
            rows += scanner.nextLine().trimEnd('\n')
        }
        charsToLetters[char] = Letter(rows)
    }

    val letterA = charsToLetters['a']!!
    charsToLetters[' '] = Letter(List(letterA.height) { " ".repeat(letterA.width) })

    return Font(charsToLetters, h)
}

/** Wrap with eights. */
fun framed(lines: List<String>): String {
    val builder = StringBuilder()

    builder.append("8".repeat(lines[0].length + 8) + "\n")
    lines.forEach { line ->
        builder.append("88  $line  88\n")
    }
    builder.append("8".repeat(lines[0].length + 8))
    return builder.toString()
}

private fun centeredLines(lines: List<String>): List<String> {
    val maxLen = lines.map { it.length }.maxOrNull()!!

    return lines.map { line ->
        val need = maxLen - line.length
        " ".repeat(need / 2) + line + " ".repeat((need + 1) / 2)
    }
}

fun authors(input: String): String {
    val roman = makeLetters(romanFontStr)
    val medium = makeLetters(mediumFontStr)

    val scanner = Scanner(input)
    val name = scanner.next() + " " + scanner.next()
    scanner.nextLine()
    val status = scanner.nextLine()

    val nameLetters = name.map {
        roman[it] ?: throw IllegalArgumentException("unknown letter $it in roman font")
    }
    val statusLetters = status.map {
        medium[it] ?: throw IllegalArgumentException("unknown letter $it in medium font")
    }

    val lines = mutableListOf<String>()

    repeat(roman.height) { i ->
        lines += nameLetters.map { it.rows[i] }.joinToString("")
    }
    repeat(medium.height) { i ->
        lines += statusLetters.map { it.rows[i] }.joinToString("")
    }

    return framed(centeredLines(lines))
}
