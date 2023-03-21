package encryptdecrypt

val ALPHABET = ((' '..'~') + ('\u00A0'..'\u00FF')).toCharArray()
val TABLE = ALPHABET.withIndex()
    .associate { (i, char) -> char to i }

fun String.mapLetters(mapper: (Char) -> Char): String = this.map(mapper).joinToString("")

fun String.encrypt(key: Int): String {
    return this.mapLetters { char ->
        val shifted = (TABLE[char]!! + key)
        ALPHABET[shifted.mod(ALPHABET.size)]
    }
}

fun String.decrypt(key: Int): String = this.encrypt(-key)

fun main() {
    val function = when (readln()) {
        "enc" -> String::encrypt
        "dec" -> String::decrypt
        else -> return
    }

    val input = readln()
    val key = readln().toInt()
    println(function.invoke(input, key))
}