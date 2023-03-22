fun main(args: Array<String>) {
    val toPrint = when (args.size) {
        3 -> args.withIndex().joinToString("\n") { (i, arg) ->
            "Argument ${i + 1}: ${arg}. It has ${arg.length} characters"
        }
        else -> "Invalid number of program arguments"
    }

    println(toPrint)
}