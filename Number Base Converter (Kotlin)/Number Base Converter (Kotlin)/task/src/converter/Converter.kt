package converter

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

const val ZERO = '0'.code
const val A = 'a'.code
val SYMBOLS = (('0'..'9') + ('a'..'z')).toCharArray()

fun BigInteger.toBase(base: Int): String {
    val b = BigInteger.valueOf(base.toLong())

    return if (this < b) {
        SYMBOLS[this.toInt()].toString()
    } else {
        val (division, remainder) = this.divideAndRemainder(b)
        "${division.toBase(base)}${SYMBOLS[remainder.toInt()]}"
    }
}

fun BigDecimal.toBase(base: Int): String {
    val converted = StringBuilder("${this.toBigInteger().toBase(base)}.${this.decimalPartToBase(base)}")

    return if (converted.last() == '.')
        converted.substring(0 until converted.lastIndex)
    else
        converted.toString()
}

fun BigDecimal.decimalPartToBase(base: Int): String {
    return this.remainder(BigDecimal.ONE).decimalPartToBase(base, 0)
}

fun BigDecimal.decimalPartToBase(base: Int, i: Int): String {
    if (this == BigDecimal.ZERO.setScale(this.scale()) || i == 5)
        return "0".repeat(this.scale() - i)

    val mult = this * base.toBigDecimal()

    return SYMBOLS[mult.toInt()] + mult.remainder(BigDecimal.ONE).decimalPartToBase(base, i + 1)
}


fun Char.toDecimalDigit(): Int {
    return when (this) {
        in '0'..'9' -> this.code - ZERO
        else -> 10 + this.lowercaseChar().code - A
    }
}

fun String.toBase(sourceBase: Int, targetBase: Int): String {
    return this.toBase10(sourceBase).toBase(targetBase)
}

fun String.toBase10(base: Int): BigDecimal {
    val (integer, decimal) = this.split('.').let { it[0] to (it.getOrElse(1) { "" }) }
    val b = BigDecimal(base)
    val power = b.pow(integer.lastIndex).setScale(5)

    val dec = (integer.asSequence() + decimal.asSequence())
        .fold(BigDecimal.ZERO to power) { (decimal, power), char ->
        decimal + char.toDecimalDigit().toBigDecimal() * power to power / b
    }.first

    return dec.setScale(if (decimal.isEmpty()) 0 else 5, RoundingMode.HALF_DOWN)
}