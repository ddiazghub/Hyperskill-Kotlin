fun main() {
    val language = readln().toInt()

    val result = when (language) {
        in 1..4 -> if (language == 2) "Yes!" else "No!"
        else -> "Unknown number"
    }

    println(result)
}