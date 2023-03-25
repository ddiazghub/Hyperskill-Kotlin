import java.math.BigDecimal
import java.math.RoundingMode     

fun main() {
    val (power, mode) = IntArray(2) {
        readln().toInt()
    }

    val number = readln().toBigDecimal()

    println(number.setScale(mode, RoundingMode.FLOOR).pow(power))
}