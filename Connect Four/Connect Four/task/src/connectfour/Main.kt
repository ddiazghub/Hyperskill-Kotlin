package connectfour

val REGEX = Regex("\\d+(\\s*[xX]\\s*)\\d+")

val PROMPTS = arrayOf(
    "Connect Four\nFirst player's name:",
    "Second player's name:"
)

fun main() {
    val (player1, player2) = PROMPTS.map {
        println(it)
        readln()
    }

    val dimensions: Pair<Int, Int>

    while (true) {
        println("Set the board dimensions (Rows x Columns)\nPress Enter for default (6 x 7)")

        val dims = when (val input = readln()) {
            "" -> 6 to 7
            else -> when (val match = REGEX.matchEntire(input.trim())) {
                null -> {
                    println("Invalid input")
                    continue
                }
                else -> {
                    val dims = match.groups[0]!!.value.split(match.groups[1]!!.value)
                        .map { it.toInt() }

                    if (dims[0] !in 5..9) {
                        println("Board rows should be from 5 to 9")
                        continue
                    }

                    if (dims[1] !in 5..9) {
                        println("Board columns should be from 5 to 9")
                        continue
                    }

                    dims[0] to dims[1]
                }
            }
        }

        dimensions = dims
        break
    }

    val numberOfGames: Int

    while (true) {
        println(
            "Do you want to play single or multiple games?\n" +
                    "For a single game, input 1 or press Enter\n" +
                    "Input a number of games:"
        )

        var n = readln()

        if (n == "")
            n = "1"

        if (Regex("[1-9]").matches(n)) {
            numberOfGames = n.toInt()
            break
        } else {
            println("Invalid Input")
        }
    }

    val game = ConnectFour(player1, player2, dimensions.first, dimensions.second, numberOfGames)
    game.start()
}