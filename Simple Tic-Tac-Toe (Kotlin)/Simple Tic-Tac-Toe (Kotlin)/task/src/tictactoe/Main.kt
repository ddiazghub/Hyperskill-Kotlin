package tictactoe

const val BOARD_SIZE = 3

class TicTacToe {
    private enum class Cell {
        X,
        O,
        EMPTY;

        override fun toString(): String {
            return when (this) {
                X -> "X"
                O -> "O"
                EMPTY -> " "
            }
        }
    }

    private enum class GameState {
        ONGOING,
        X_WINS,
        O_WINS,
        DRAW
    }

    private enum class MoveResult {
        OCCUPIED,
        OUT_OF_BOUNDS,
        OK
    }

    private val board: Array<Array<Cell>> = Array(BOARD_SIZE) {
        Array(BOARD_SIZE) { Cell.EMPTY }
    }

    fun start() {
        println(this)
        var player1 = true

        while (true) {
            try {
                val (i, j) = readln()
                    .split(' ')
                    .map { it.toInt() - 1 }

                val toPrint = when (this.move(i, j, player1)) {
                    MoveResult.OCCUPIED -> "This cell is occupied! Choose another one!"
                    MoveResult.OUT_OF_BOUNDS -> "Coordinates should be from 1 to 3!"
                    MoveResult.OK -> this.toString()
                }

                println(toPrint)
                player1 = !player1

                val state = when (this.getState()) {
                    GameState.DRAW -> "Draw"
                    GameState.X_WINS -> "X wins"
                    GameState.O_WINS -> "O wins"
                    else -> continue
                }

                println(state)
                return
            } catch (e: Exception) {
                println("You should enter numbers!")
            }
        }
    }

    private fun move(i: Int, j: Int, player1: Boolean): MoveResult {
        val max = this.board.lastIndex

        if (i > max || j > max)
            return MoveResult.OUT_OF_BOUNDS

        if (this.board[i][j] != Cell.EMPTY)
            return MoveResult.OCCUPIED

        this.board[i][j] = if (player1) Cell.X else Cell.O
        return MoveResult.OK
    }

    private fun getState(): GameState {
        for (i in board.indices) {
            if (this.rowWinCondition(i) || this.columnWinCondition(i))
                return this.getWinner(i, i)
        }

        if (this.diagonalWinCondition() || this.diagonalWinCondition(false))
            return this.getWinner(1, 1)

        val remaining = this.board
            .flatten()
            .count { it == Cell.EMPTY }

        return if (remaining == 0) GameState.DRAW
            else GameState.ONGOING
    }

    private fun rowWinCondition(row: Int): Boolean {
        val r = this.board[row]

        return r[0] != Cell.EMPTY && r[1] == r[0] && r[2] == r[0]
    }

    private fun columnWinCondition(col: Int): Boolean {
        val first = this.board[0][col]

        return first != Cell.EMPTY && board[1][col] == first && board[2][col] == first
    }

    private fun diagonalWinCondition(primary: Boolean = true): Boolean {
        val mid = this.board[1][1]

        return mid != Cell.EMPTY && if (primary) {
            board[0][0] == mid && board[2][2] == mid
        } else {
            board[2][0] == mid && board[0][2] == mid
        }
    }

    private fun getWinner(i: Int, j: Int): GameState {
        return if (this.board[i][j] == Cell.X) {
            GameState.X_WINS
        } else {
            GameState.O_WINS
        }
    }

    override fun toString(): String {
        val boardString = board.asSequence()
            .map { row -> "| ${row.joinToString(" ")} |" }
            .joinToString("\n")

        return "---------\n${boardString}\n---------"
    }
}

fun main() {
    val game = TicTacToe()
    game.start()
}