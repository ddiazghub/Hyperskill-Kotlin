import java.math.BigDecimal
import java.math.RoundingMode

fun main() {             
    val number = readln().toBigDecimal()
    val scale = readln().toInt()

    println(number.setScale(scale, RoundingMode.HALF_DOWN))
}