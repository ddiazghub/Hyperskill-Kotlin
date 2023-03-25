package converter // Do not delete this line

fun readln(prompt: String): String {
    print(prompt)
    return readln()
}

fun main() {
    while (true) {
        val (source, target) = when (val input = readln("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")) {
            "/exit" -> return
            else -> input.split(' ').map { it.toInt() }
        }

        while (true) {
            val result = when (val input = readln("Enter number in base $source to convert to base $target (To go back type /back) ")) {
                "/back" -> break
                else -> input.toBase(source, target)
            }

            println("Conversion result: $result")
            println()
        }

        println()
    }
}