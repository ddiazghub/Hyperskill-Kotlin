import java.math.BigInteger

fun main() {
    val numbers = Array(2) {
        BigInteger(readln())
    }

    val sum = numbers.sumOf { it }
    val percents = numbers.map { it * BigInteger.valueOf(100) / sum }
    println("${percents[0]}% ${percents[1]}%")
}