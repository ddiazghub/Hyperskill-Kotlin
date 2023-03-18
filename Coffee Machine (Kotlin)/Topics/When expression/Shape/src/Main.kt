fun main(args: Array<String>) {
    val n = readln().toInt()

    val shape = when (n) {
        1 -> "square"
        2 -> "circle"
        3 -> "triangle"
        4 -> "rhombus"
        else -> return println("There is no such shape!")
    }

    println("You have chosen a $shape")
}