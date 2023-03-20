package chess

const val BOARD_SEP = "  +---+---+---+---+---+---+---+---+"
const val BOARD_FOOTER = "    a   b   c   d   e   f   g   h"
const val BOARD_SIZE = 8
const val A = 'a'.code
val regex = Regex("([a-h][1-8]){2}")

fun Boolean.toMove(): Chess.MoveResult {
    return if (this) Chess.MoveResult.OK
    else Chess.MoveResult.INVALID_MOVE
}

class Chess(private val player1: String, val player2: String) {
    private enum class Move {
        FORWARD1,
        FORWARD2,
        CAPTURE_LEFT,
        CAPTURE_RIGHT,
        INVALID;

        companion object {
            fun new(from: Vector2, _to: Vector2, player1: Boolean): Move {
                var delta = _to - from
                delta = if (player1) delta * Vector2(1, -1) else delta

                return when (delta) {
                    Vector2(0, 1) -> FORWARD1
                    Vector2(0, 2) -> FORWARD2
                    Vector2(-1, 1) -> CAPTURE_LEFT
                    Vector2(1, 1) -> CAPTURE_RIGHT
                    else -> INVALID
                }
            }
        }
    }

    enum class MoveResult {
        PARSE_ERROR,
        NO_PAWN_AT_START,
        INVALID_MOVE,
        OK
    }

    private sealed class Cell {
        abstract class Pawn(var firstMove: Boolean = true): Cell()
        class White : Pawn()
        class Black : Pawn()
        object Empty : Cell()


        override fun toString(): String {
            return when (this) {
                is White -> "W"
                is Black -> "B"
                else -> " "
            }
        }
    }

    private var player1Pawns: MutableMap<Cell.Pawn, Vector2> = mutableMapOf()
    private var player2Pawns: MutableMap<Cell.Pawn, Vector2> = mutableMapOf()

    private val vulnerable: ArrayDeque<Pair<Int, Vector2>> = ArrayDeque(2)

    private val board: Array<Array<Cell>> = Array(BOARD_SIZE) {
        val values: (Int) -> Cell = when (it) {
            1 -> ({
                val pawn = Cell.Black()
                player2Pawns[pawn] = Vector2(it, 1)
                pawn
            })
            6 -> ({
                val pawn = Cell.White()
                player1Pawns[pawn] = Vector2(it, 6)
                pawn
            })
            else -> ({ Cell.Empty })
        }

        Array(BOARD_SIZE, values)
    }

    fun start() {
        println(this)
        var player1 = true

        while (true) {
            if (!this.ongoing(player1)) {
                println("Stalemate!")
                break
            }

            println("${if (player1) this.player1 else this.player2}'s turn:")

            when (val move = readln()) {
                "exit" -> break
                else -> when (this.move(move, player1)) {
                    MoveResult.PARSE_ERROR, MoveResult.INVALID_MOVE -> println("Invalid Input")
                    MoveResult.NO_PAWN_AT_START -> println("No ${if (player1) "white" else "black"} pawn at ${move.substring(0..1)}")
                    MoveResult.OK -> {
                        player1 = !player1
                        println(this)

                        for (i in this.vulnerable.indices) {
                            val pawn = this.vulnerable[i]
                            this.vulnerable[i] = pawn.copy(first = pawn.first - 1)
                        }

                        if ((this.vulnerable.lastOrNull() ?: Pair(null, null)).first == 0) {
                            this.vulnerable.removeLast()
                        }

                        if (this.player1Wins()) {
                            println("White Wins!")
                            break
                        }

                        if (this.player2Wins()) {
                            println("Black Wins!")
                            break
                        }
                    }
                }
            }
        }

        println("Bye!")
    }

    private fun move(move: String, player1: Boolean = true): MoveResult {
        val (from, _to) = this.parseMove(move) ?: return MoveResult.PARSE_ERROR

        return this.checkValidMove(from, _to, player1).let {
            if (it == MoveResult.OK)
                doMove(from, _to)

            it
        }
    }

    override fun toString(): String {
        return this.board
            .asSequence()
            .withIndex()
            .map { (i, row) ->
                "${BOARD_SEP}\n${BOARD_SIZE - i} | ${row.joinToString(" | ")} |"
            }.joinToString("\n") + "\n$BOARD_SEP\n$BOARD_FOOTER"
    }

    private fun doMove(from: Vector2, _to: Vector2) {
        val pawn = this.board[from.y][from.x] as Cell.Pawn

        if (pawn.firstMove) {
            this.vulnerable.add(2 to _to)
            pawn.firstMove = false
        }

        this.board[from.y][from.x] = Cell.Empty

        when (val cell = this.board[_to.y][_to.x]) {
            is Cell.White -> this.player1Pawns.remove(cell)
            is Cell.Black -> this.player2Pawns.remove(cell)
            else -> {}
        }

        this.board[_to.y][_to.x] = pawn
        (if (pawn is Cell.White) this.player1Pawns else this.player2Pawns)[pawn] = _to
    }

    private fun checkValidMove(from: Vector2, _to: Vector2, player1: Boolean = true): MoveResult {
        val move = Move.new(from, _to, player1)
        val fromCell = board[from.y][from.x]
        val toCell = board[_to.y][_to.x]

        return when {
            fromCell is Cell.White && player1 || fromCell is Cell.Black && !player1 -> {
                when (move) {
                    Move.FORWARD1 -> (toCell is Cell.Empty).toMove()
                    Move.FORWARD2 -> ((fromCell as Cell.Pawn).firstMove && this.pathIsEmpty(from, _to)).toMove()
                    Move.CAPTURE_LEFT, Move.CAPTURE_RIGHT -> (this.enPassant(from, _to, player1) || (player1 && toCell is Cell.Black) || (!player1 && toCell is Cell.White)).toMove()
                    Move.INVALID -> MoveResult.INVALID_MOVE
                }
            }
            else -> MoveResult.NO_PAWN_AT_START
        }
    }

    private fun enPassant(from: Vector2, _to: Vector2, player1: Boolean): Boolean {
        return when ((this.vulnerable.lastOrNull() ?: Pair(null, null)).second) {
            Vector2(from.y, _to.x) -> {
                when (val cell = this.board[from.y][_to.x]) {
                    is Cell.White -> if (player1) return false
                        else this.player1Pawns.remove(cell)
                    is Cell.Black -> if (!player1) return false
                    else -> this.player2Pawns.remove(cell)
                }

                this.board[from.y][_to.x] = Cell.Empty
                true
            }
            else -> false
        }
    }

    private fun pathIsEmpty(start: Vector2, end: Vector2): Boolean {
        val range = if (start.y < end.y) {
            start.y + 1..end.y
        } else {
            start.y - 1 downTo end.y
        }

        for (y in range) {
            if (this.board[y][start.x] !is Cell.Empty)
                return false
        }

        return true
    }

    private fun player1Wins(): Boolean {
        return this.player2Pawns.isEmpty() || this.board.first().any { it is Cell.White }
    }

    private fun player2Wins(): Boolean {
        return this.player1Pawns.isEmpty() || this.board.last().any { it is Cell.Black }
    }

    private fun ongoing(player1: Boolean): Boolean {
        val (direction, enemy) = if (player1) -1 to Cell.Black::class else 1 to Cell.White::class
        val pawns = if (player1) this.player1Pawns else this.player2Pawns

        return pawns.values.any { (x, y) ->
            board[y + direction][x] == Cell.Empty ||
                board[y + direction].getOrElse(x - 1) { Cell.Empty }::class == enemy ||
                    board[y + direction].getOrElse(x - 1) { Cell.Empty }::class == enemy
        }
    }

    private fun parseMove(move: String): Pair<Vector2, Vector2>? {
        if (!regex.matches(move))
            return null

        return Pair(parsePosition(move[0], move[1]), parsePosition(move[2], move[3]))
    }

    private fun parsePosition(file: Char, rank: Char): Vector2 = Vector2(file.code - A, BOARD_SIZE - rank.digitToInt())
}