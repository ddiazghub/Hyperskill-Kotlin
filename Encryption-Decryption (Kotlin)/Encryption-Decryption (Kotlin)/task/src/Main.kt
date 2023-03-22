package encryptdecrypt

import java.io.File
import java.io.IOException
import java.io.PrintStream

const val ALPHA_SIZE = 26
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

fun String.shiftEncrypt(key: Int): String {
    return this.mapLetters { char ->
        if (char.isLetter()) {
            val base = if (char.isUpperCase()) 'A' else 'a'
            val shifted = (char.code - base.code + key)
            base + shifted.mod(ALPHA_SIZE)
        } else char
    }
}

fun String.shiftDecrypt(key: Int): String = this.shiftEncrypt(-key)

fun main(args: Array<String>) {
    try {
        val argsMap = mutableMapOf(
            "alg" to "shift",
            "mode" to "enc",
            "key" to "0",
            "data" to ""
        )

        for ((option, arg) in args.asSequence().chunked(2)) {
            argsMap[option.substring(1)] = arg
        }

        val function = when (argsMap["alg"] to argsMap["mode"]) {
            "shift" to "enc" -> String::shiftEncrypt
            "shift" to "dec" -> String::shiftDecrypt
            "unicode" to "enc" -> String::encrypt
            "unicode" to "dec" -> String::decrypt
            else -> throw Exception("No such algorithm or mode")
        }

        val data = when (val data = argsMap["data"]) {
            "" -> argsMap["in"]?.let { File(it).readText() } ?: ""
            else -> data!!
        }

        val encoded = function(data, argsMap["key"]!!.toInt())

        val out = when (val outfile = argsMap["out"]) {
            null -> System.out
            else -> {
                val file = File(outfile)

                if (!file.exists())
                    file.createNewFile()

                PrintStream(file)
            }
        }

        out.use { it.println(encoded) }
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}