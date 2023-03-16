// write your function here

fun main() {
    val numbers = Array(3) {
        readln().toInt()
    }

    println(sum(numbers))
}

fun sum(numbers: Array<Int>) = numbers.sum()