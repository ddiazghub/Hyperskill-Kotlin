package chucknorris

import java.lang.NumberFormatException

const val ASCII_BITS = 7

sealed class NorrisException(message: String) : RuntimeException(message) {
    object InvalidDecodeInput : NorrisException("Encoded string is not valid.")
}

fun String.norrisEncode(): String {
    if (this.isEmpty())
        return this

    var i = 0
    val encoded = StringBuilder()

    val ascii = this.asSequence().flatMap { char ->
        char.code.toString(2)
            .padStart(ASCII_BITS, '0')
            .asSequence()
    }.joinToString("")

    while (i < ascii.length) {
        encoded.append(if (ascii[i] == '0') "00 " else "0 ")

        val seriesLength = ascii.substring(i)
            .takeWhile { it == ascii[i] }
            .count()

        encoded.append("0".repeat(seriesLength)).append(' ')
        i += seriesLength
    }

    return encoded.substring(0 until encoded.lastIndex)
}

fun String.norrisDecode(): String {
    try {
        return this
            .trim()
            .splitToSequence(' ')
            .chunked(2)
            .flatMap { (bit, count) ->
                val bt = when (bit) {
                    "0" -> "1"
                    "00" -> "0"
                    else -> throw NorrisException.InvalidDecodeInput
                }

                val ct = count.count { if (it == '0') true else throw NorrisException.InvalidDecodeInput }
                bt.repeat(ct).asSequence()
            }
            .chunked(ASCII_BITS)
            .map { chunk -> if (chunk.size == 7) {
                chunk.joinToString("").toInt(2)
            } else {
                throw NorrisException.InvalidDecodeInput
            } }
            .joinToString("", transform = Character::toString)
    } catch (e: NumberFormatException) {
        throw NorrisException.InvalidDecodeInput
    }
}

fun main() {
    while (true) {
        try {
            println("Please input operation (encode/decode/exit):")
            val operation = readln()

            val (print1, print2, function) = when (operation) {
                "encode" -> Triple("", "Encoded", String::norrisEncode)
                "decode" -> Triple("encoded ", "Decoded", String::norrisDecode)
                "exit" -> {
                    println("Bye"); return
                }
                else -> throw RuntimeException("There is no '$operation' operation")
            }

            println("Input ${print1}string:")
            val input = readln()
            val decoded = function.invoke(input)
            println("$print2 string:")
            println(decoded)
        } catch(e: Exception) {
            println(e.message)
        }

        println()
    }
}