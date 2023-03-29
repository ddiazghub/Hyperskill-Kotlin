package indigo

import kotlin.random.Random

class Indigo {
    val deck = Cards.deck()
    val table = Cards(deck, 4)
    val player = Player(deck)
    val cpu = AIPlayer(deck)

    fun start() {
        println("Indigo Card Game")
        var first: String

        do first = readln("Play first?").lowercase()
        while (first !in arrayOf("yes", "no"))

        val initial = if (first == "yes") player else cpu
        var playing = initial
        var lastWinner: Player? = null

        println("Initial cards on the table: $table")

        loop@while (true) {
            val top = this.table.top() ?: "00"
            println()

            when (val n = table.size) {
                0 -> println("No cards on the table")
                else -> println("$n cards on the table, and the top card is $top")
            }

            if (player.hand.size == 0 && cpu.hand.size == 0)
                break

            when (playing.move(table)) {
                MoveResult.Win -> {
                    lastWinner = playing
                    printScore()
                }
                MoveResult.Exit -> return println("Game Over")
                MoveResult.Normal -> {}
            }

            playing.deal(deck)
            playing = if (playing === player) cpu else player
        }

        lastWinner?.win(table) ?: initial.win(table)
        player.compare(cpu, initial === player)
        printScore()
        println("Game Over")
    }

    fun printScore() {
        println("Score: Player ${player.score} - Computer ${cpu.score}")
        println("Cards: Player ${player.won} - Computer ${cpu.won}")
    }
}