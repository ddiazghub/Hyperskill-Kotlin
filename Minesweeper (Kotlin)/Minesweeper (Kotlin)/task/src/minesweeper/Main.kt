package minesweeper

import kotlin.random.Random
import kotlin.system.exitProcess

const val WIDTH = 9
const val HEIGHT = 9

class MineSweeper(val width: Int, val height: Int, val mineCount: Int = 0) {
    private enum class CellState {
        MARKED,
        EXPLORED,
        NONE
    }

    private sealed class Cell(var state: CellState = CellState.NONE) {
        class Mine : Cell()
        class Empty(var adjacentMines: Int = 0) : Cell()

        override fun toString(): String {
            return when (this.state) {
                CellState.NONE -> "."
                CellState.MARKED -> "*"
                CellState.EXPLORED -> when (this) {
                    is Mine -> "X"
                    is Empty -> if (this.adjacentMines > 0) this.adjacentMines.toString()
                        else "/"
                }
            }
        }
    }

    private val board: Array<Array<Cell>> = Array(height) {
        Array(width) { Cell.Empty() }
    }

    private val mines: MutableSet<Pair<Int, Int>> = mutableSetOf()
    private val marks: MutableSet<Pair<Int, Int>> = mutableSetOf()
    private var unexploredCells = this.width * this.height - this.mineCount

    init {
        for (i in 0 until this.mineCount) {
            var cell = this.randomCell()

            while (this.board[cell.second][cell.first] is Cell.Mine) {
                cell = this.randomCell()
            }

            this.addMine(cell.first, cell.second)
        }
    }

    fun start() {
        while (!this.playerWon()) {
            println("Set/unset mine marks or claim a cell as free:")

            try {
                val (xPos, yPos, command) = readln().split(' ')
                val x = xPos.toInt() - 1
                val y = yPos.toInt() - 1

                when (command) {
                    "mine" -> if (this.mark(x, y)) println(this)
                    else println("The cell is already marked!")
                    "free" -> if (this.free(x, y)) println(this)
                    else println("The cell is either marked or already explored!")
                }
            } catch (e: Exception) {
                println("Input must be in the format: {x} {y} {action}")
            }
        }

        println("Congratulations! You found all the mines!")
    }

    private fun addMine(x: Int, y: Int) {
        this.mines.add(Pair(x, y))
        this.board[y][x] = Cell.Mine()

        for (i in maxOf(0, y - 1)..minOf(height - 1, y + 1)) {
            for (j in maxOf(0, x - 1)..minOf(width - 1, x + 1)) {
                when (val cell = this.board[i][j]) {
                    is Cell.Empty -> cell.adjacentMines++
                    else -> {}
                }
            }
        }
    }

    private fun randomCell(): Pair<Int, Int> = Pair(Random.nextInt(width), Random.nextInt(height))

    private fun mark(x: Int, y: Int): Boolean {
        val cell = this.board[y][x]

        when (cell.state) {
            CellState.NONE -> {
                cell.state = CellState.MARKED
                this.marks.add(Pair(x, y))
            }
            CellState.MARKED -> {
                cell.state = CellState.NONE
                this.marks.remove(Pair(x, y))
            }
            CellState.EXPLORED -> return false
        }

        return true
    }

    private fun free(x: Int, y: Int): Boolean {
        val cell = this.board[y][x]

        when (cell.state) {
            CellState.EXPLORED, CellState.MARKED -> return false
            CellState.NONE -> when (cell) {
                is Cell.Mine -> playerLost()
                is Cell.Empty -> {
                    cell.state = CellState.EXPLORED
                    this.unexploredCells--

                    if (cell.adjacentMines == 0) {
                        this.freeAdjacent(x, y)
                    }
                }
            }
        }

        return true
    }

    private fun freeAdjacent(x: Int, y: Int) {
        for (i in maxOf(0, y - 1)..minOf(height - 1, y + 1)) {
            for (j in maxOf(0, x - 1)..minOf(width - 1, x + 1)) {
                val cell = this.board[i][j]

                if (cell.state == CellState.MARKED) {
                    cell.state = CellState.NONE
                    this.marks.remove(Pair(j, i))
                }

                this.free(j, i)
            }
        }
    }

    private fun playerWon(): Boolean = this.unexploredCells == 0 || this.marks == this.mines

    private fun playerLost() {
        for ((x, y) in this.mines) {
            this.board[y][x].state = CellState.EXPLORED
        }

        println(this)
        println("You stepped on a mine and failed!")
        exitProcess(0)
    }

    override fun toString(): String {
        val separator = "—".repeat(this.width)
        val board = " |${(1..this.width).joinToString("")}|\n-|$separator|\n"

        return board + this.board.withIndex().map { (i, row) ->
            val r = row.map { cell -> cell.toString() }.joinToString("")
            "${i + 1}|$r|\n"
            }.joinToString("") + "—│$separator│"

    }
}

fun main() {
    println("How many mines do you want on the field?")
    val mines = readln().toInt()
    val game = MineSweeper(WIDTH, HEIGHT, mines)
    println(game)
    game.start()
}