fun lettersNotIn(word: String): Sequence<Char> {
    val letters = ('a'..'z').toMutableSet()

    for (letter in word) {
        letters.remove(letter)
    }

    return letters.asSequence()
}

fun main() {
    val word = readln()
    println(lettersNotIn(word).joinToString(""))
}