import kotlin.math.sqrt

const val PI = 3.14

fun triangle() {
    val (a, b, c) = Array(3) { readln().toDouble() }
    val s = (a + b + c) / 2
    println(sqrt(s * (s - a) * (s - b) * (s - c)))
}

fun circle() {
    val r = readln().toDouble()
    println(PI * r * r)
}

fun rectangle() {
    val (a, b) = Array(2) { readln().toDouble() }
    println(a * b)
}

fun main() {
    val shape = readln()

    when (shape) {
        "triangle" -> triangle()
        "circle" -> circle()
        "rectangle" -> rectangle()
    }
}