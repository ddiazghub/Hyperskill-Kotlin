import java.math.BigDecimal
import java.math.RoundingMode

fun main() {
    val numbers = Array(3) {
        readln().toBigDecimal()
    }

    val size = numbers.size.toBigDecimal()

    println(numbers.sumOf { it / size }.setScale(0, RoundingMode.DOWN))
}