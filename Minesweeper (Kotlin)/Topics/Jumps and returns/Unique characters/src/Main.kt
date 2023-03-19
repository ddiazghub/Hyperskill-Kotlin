fun charFrequencies(word: String): Map<Char, Int> {
    val frequencies = ('a'..'z')
        .associateWith { ch -> 0 }
        .toMutableMap()

    for (char in word) {
        frequencies[char] = frequencies[char]!! + 1
    }

    return frequencies
}

fun main() {
    val input = readln()

    val count = charFrequencies(input)
        .asSequence()
        .filter { (_, count) -> count == 1 }
        .map { (ch, _) -> ch }
        .count()

    println(count)
}