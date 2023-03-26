package connectfour

fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }

data class Vector2(val x: Int, val y: Int) {
    operator fun plus(other: Vector2) = Vector2(x + other.x, y + other.y)
    operator fun minus(other: Vector2) = Vector2(x - other.x, y - other.y)
}

class ConnectFour(val player1: String, val player2: String, val rows: Int, val columns: Int, var numberOfGames: Int) {
    enum class MoveResult {
        Ok,
        Win,
        OutOfBounds,
        ColumnFull,
        Draw
    }

    enum class Cell(val symbol: Char) {
        Red('o'),
        Yellow('*'),
        Empty(' ');

        fun belongsTo(player1: Boolean): Boolean = player1 && this == Red || !player1 && this == Yellow
        override fun toString(): String = this.symbol.toString()
    }

    private var board: Array<Array<Cell>> = this.emptyBoard()

    private var moves: Int = 0
    private var player1Score: Int = 0
    private var player2Score: Int = 0

    private var winConditions: Array<Pair<Vector2, (Vector2) -> Boolean>> = arrayOf(
        Vector2(1, 0) to { it.x in 0 until this.columns },
        Vector2(0, 1) to { it.y in 0 until this.rows },
        Vector2(1, 1) to { it.x in 0 until this.columns && it.y in 0 until this.rows},
        Vector2(1, -1) to { it.x in 0 until this.columns && it.y in 0 until this.rows}
    )

    fun start() {
        println("$player1 VS $player2")
        println("$rows X $columns board")
        println(if (numberOfGames > 1) "Total $numberOfGames games" else "Single game")
        var player1 = true

        for (i in 0 until this.numberOfGames) {
            if (numberOfGames > 1)
                println("Game #${i + 1}")

            printBoard()

            while (true) {
                val player = if (player1) this.player1 else this.player2
                println("$player's turn:")
                when (val input = readln()) {
                    "end" -> break
                    else -> try {
                        when (this.move(input.toInt() - 1, player1)) {
                            MoveResult.Ok -> {
                                printBoard()
                                player1 = !player1
                            }
                            MoveResult.OutOfBounds -> println("The column number is out of range (1 - ${this.columns})")
                            MoveResult.ColumnFull -> println("Column $input is full")
                            MoveResult.Win -> {
                                printBoard()
                                println("Player $player won")

                                if (player1) this.player1Score += 2
                                else this.player2Score += 2

                                break
                            }
                            MoveResult.Draw -> {
                                printBoard()
                                println("It is a draw")
                                this.player1Score++
                                this.player2Score++

                                break
                            }
                        }
                    } catch (e: NumberFormatException) {
                        println("Incorrect column number")
                    }
                }
            }

            if (numberOfGames > 1)
                println("Score\n${this.player1}: $player1Score $player2: $player2Score")

            player1 = i % 2 > 0
            this.board = this.emptyBoard()
            this.moves = 0
        }

        println("Game over!")
    }

    fun move(column: Int, player1: Boolean): MoveResult {
        return if (column in 0 until this.columns) {
            val row = (this.rows - 1 downTo 0)
                .find { this.board[it][column] == Cell.Empty }

            when (row) {
                null -> MoveResult.ColumnFull
                else -> {
                    this.board[row][column] = if (player1) Cell.Red else Cell.Yellow
                    this.moves += 1

                    if (this.hasWon(Vector2(column, row), player1))
                        MoveResult.Win
                    else if (this.moves == this.rows * this.columns)
                        MoveResult.Draw
                    else
                        MoveResult.Ok
                }
            }
        } else MoveResult.OutOfBounds
    }

    fun hasWon(movePosition: Vector2, player1: Boolean): Boolean {
        return this.winConditions
            .any {
                winCondition(movePosition, it.first, player1, it.second)
            }
    }

    fun emptyBoard(): Array<Array<Cell>> {
        return Array(this.rows) {
            Array(this.columns) { Cell.Empty }
        }
    }

    fun printBoard() {
        val board = "${header(this.columns)}\n" +
                this.board.joinToString("\n") { row(it) } +
            "\n${footer(this.columns)}"

        println(board)
    }

    private fun winCondition(initial: Vector2, direction: Vector2, player1: Boolean, condition: (Vector2) -> Boolean): Boolean {
        val predicate = { position: Vector2 -> this.board[position.y][position.x].belongsTo(player1) }
        val direction1 = generateSequence(initial) { it + direction }
        val direction2 = generateSequence(initial) { it - direction }
        val count1 = direction1.takeWhile { condition(it) && predicate(it) }.count()
        val count2 = direction2.takeWhile { condition(it) && predicate(it) }.count()

        return (count1 + count2 - 1) > 3
    }

    companion object {
        fun header(columns: Int): String {
            return " " + (1..columns)
                .joinToString(" ")
        }

        fun row(row: Array<Cell>): String {
            return row.joinToString("") {
                "║${it}"
            } + '║'
        }

        fun footer(columns: Int): String {
            return "╚═${arrayOf('╩').asSequence().repeat().take(columns - 1).joinToString("═")}═╝"
        }
    }
}