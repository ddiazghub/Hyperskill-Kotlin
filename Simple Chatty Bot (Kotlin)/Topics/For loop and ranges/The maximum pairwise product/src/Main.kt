fun main() {
    val n = readln().toInt()

    val numbers = Array(n) {
        readln().toInt()
    }

    numbers.sortDescending()

    println(
        numbers.getOrElse(0) { 0 } * numbers.getOrElse(1) { 1 }
    )
}