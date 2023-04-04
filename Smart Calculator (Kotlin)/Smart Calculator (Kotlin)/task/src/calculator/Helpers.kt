package calculator

fun readlns(): Sequence<String> = sequence {
    while (true) {
        val input = readln().trim()
        if (input.isNotEmpty()) yield(input)
    }
}