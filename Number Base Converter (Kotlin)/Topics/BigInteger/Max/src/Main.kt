import java.math.BigInteger

fun main() {
    val numbers = Array(2) {
        BigInteger(readln())
    }

    println(numbers[0].max(numbers[1]))
}