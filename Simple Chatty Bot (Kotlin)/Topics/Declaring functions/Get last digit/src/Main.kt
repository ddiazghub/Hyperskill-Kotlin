import kotlin.math.absoluteValue

// write your code here
fun getLastDigit(number: Int) = (number % 10).absoluteValue

/* Do not change code below */
fun main() {
    val a = readln().toInt()
    println(getLastDigit(a))
}