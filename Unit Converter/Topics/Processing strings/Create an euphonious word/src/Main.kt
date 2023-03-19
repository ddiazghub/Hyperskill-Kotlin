val VOWELS = setOf('a', 'e', 'i', 'o', 'u', 'y')

enum class LetterType {
    VOWEL,
    CONSONANT;

    companion object {
        fun new(letter: Char): LetterType {
            return if (VOWELS.contains(letter)) VOWEL
                else CONSONANT
        }
    }
}

fun main() {
    var count = 0
    var type = LetterType.CONSONANT
    var consecutives = 0

    for (letter in readln()) {
        val letterType = LetterType.new(letter)

        if (type != letterType) {
            count += (consecutives - 1) / 2
            type = letterType
            consecutives = 0
        }

        consecutives++
    }

    count += (consecutives - 1) / 2

    println(count)
}