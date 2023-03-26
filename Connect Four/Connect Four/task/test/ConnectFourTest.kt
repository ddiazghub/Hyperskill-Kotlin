import org.hyperskill.hstest.dynamic.DynamicTest
import org.hyperskill.hstest.stage.StageTest
import org.hyperskill.hstest.testcase.CheckResult
import org.hyperskill.hstest.testing.TestedProgram

class ConnectFourTest : StageTest<Any>() {

    @DynamicTest
    fun cf23Test(): CheckResult {
        val whiteDiscs = mutableListOf<Pair<Int, Int>>()
        val blackDiscs = mutableListOf<Pair<Int, Int>>()
        val inputNoOfGames = listOf<String>("0", "X", "i")

        val main = TestedProgram()
        var outputString = main.start().trim()

        var position = checkOutput(outputString.lowercase(), 0, "connect four")
        if (position == -1) return CheckResult(false, "Wrong program title.")
        position = checkOutput(outputString.lowercase(), position, "first player's name:")
        if (position == -1) return CheckResult(false, "Wrong prompt for first player's name.")

        outputString = main.execute("Anna").trim()
        position = checkOutput(outputString.lowercase(), 0, "second player's name:")
        if (position == -1) return CheckResult(false, "Wrong prompt for second player's name.")

        outputString = main.execute("Joan").trim()
        position = checkOutput(outputString.lowercase(), 0,
            "set the board dimensions (rows x columns)", "press enter for default (6 x 7)")
        if (position == -1) return CheckResult(false, "Wrong prompt for board dimensions.")

        outputString = main.execute("5x6").trim()

        position = checkOutput(outputString.lowercase(), 0, "do you want to play single or multiple games?",
            "for a single game, input 1 or press enter", "input a number of games:")
        if (position == -1) return CheckResult(false, "Wrong prompt for multiple games.")

        for (input in inputNoOfGames) {
            outputString = main.execute(input).trim()
            position = checkOutput(
                outputString.lowercase(), 0, "invalid input",
                "do you want to play single or multiple games?",
                "for a single game, input 1 or press enter", "input a number of games:"
            )
            if (position == -1) return CheckResult(false, "Wrong output after input of invalid number of games.")
        }

        main.stop()

        return CheckResult.correct()
    }

    @DynamicTest
    fun cf16Test(): CheckResult {
        val main = TestedProgram()
        var outputString = main.start().trim()

        var position = checkOutput(outputString.lowercase(), 0, "connect four")
        if ( position  == -1 ) return CheckResult(false, "Wrong program title.")
        position = checkOutput(outputString.lowercase(), position, "first player's name:")
        if ( position  == -1 ) return CheckResult(false, "Wrong prompt for first player's name.")

        outputString = main.execute("Anna").trim()
        position = checkOutput(outputString.lowercase(), 0, "second player's name:")
        if ( position  == -1 ) return CheckResult(false, "Wrong prompt for second player's name.")

        outputString = main.execute("Joan").trim()
        position = checkOutput(outputString.lowercase(), 0,
            "set the board dimensions (rows x columns)", "press enter for default (6 x 7)")
        if ( position  == -1 ) return CheckResult(false, "Wrong prompt for board dimensions.")

        var inputDimensions = mutableListOf<String>("4x5", "4X5", "10x6", "12x6")
        for (input in inputDimensions) {
            outputString = main.execute(input).trim()
            position = checkOutput(outputString.lowercase(), 0,
                "board rows should be from 5 to 9",
                "set the board dimensions (rows x columns)", "press enter for default (6 x 7)")
            if (position == -1) return CheckResult(false,
                "Wrong error message for out of range row size.")
        }

        inputDimensions = mutableListOf<String>("6x1", "7X4", "8x10", "9x30")
        for (input in inputDimensions) {
            outputString = main.execute(input).trim()
            position = checkOutput(outputString.lowercase(), 0,
                "board columns should be from 5 to 9",
                "set the board dimensions (rows x columns)", "press enter for default (6 x 7)")
            if (position == -1) return CheckResult(false, "Wrong error message for out of range column size.")
        }

        main.stop()
        return CheckResult.correct()
    }

    @DynamicTest
    fun cf17Test(): CheckResult {
        val main = TestedProgram()
        var outputString = main.start().trim()

        var position = checkOutput(outputString.lowercase(), 0, "connect four")
        if ( position  == -1 ) return CheckResult(false, "Wrong program title.")
        position = checkOutput(outputString.lowercase(), position, "first player's name:")
        if ( position  == -1 ) return CheckResult(false, "Wrong prompt for first player's name.")

        outputString = main.execute("Anna").trim()
        position = checkOutput(outputString.lowercase(), 0, "second player's name:")
        if ( position  == -1 ) return CheckResult(false, "Wrong prompt for second player's name.")

        outputString = main.execute("Joan").trim()
        position = checkOutput(outputString.lowercase(), 0,
            "set the board dimensions (rows x columns)", "press enter for default (6 x 7)")
        if ( position  == -1 ) return CheckResult(false, "Wrong prompt for board dimensions.")

        val inputDimensions = mutableListOf<String>("6x", "X5", "10k6", "12Z6", "a  7x9", "5x8  t")
        for (input in inputDimensions) {
            outputString = main.execute(input).trim()
            position = checkOutput(outputString.lowercase(), 0,
                "invalid input", "set the board dimensions (rows x columns)", "press enter for default (6 x 7)")
            if (position == -1) return CheckResult(false,
                "Wrong error message for out of range row size.")
        }

        main.stop()
        return CheckResult.correct()
    }

    @DynamicTest
    fun cf18Test(): CheckResult {
        val whiteDiscs = mutableListOf<Pair<Int, Int>>()
        val blackDiscs = mutableListOf<Pair<Int, Int>>()
        val inputDimensions = listOf<String>("5x5", "9X9", "9X5", "", "   7   x   9   ", "  8  X   6   ", "\t  9 \tX \t5  \t ")

        for (input in inputDimensions) {
            val main = TestedProgram()
            var outputString = main.start().trim()

            var position = checkOutput(outputString.lowercase(), 0, "connect four")
            if (position == -1) return CheckResult(false, "Wrong program title.")
            position = checkOutput(outputString.lowercase(), position, "first player's name:")
            if (position == -1) return CheckResult(false, "Wrong prompt for first player's name.")

            outputString = main.execute("Anna").trim()
            position = checkOutput(outputString.lowercase(), 0, "second player's name:")
            if (position == -1) return CheckResult(false, "Wrong prompt for second player's name.")

            outputString = main.execute("Joan").trim()
            position = checkOutput(outputString.lowercase(), 0,
                "set the board dimensions (rows x columns)", "press enter for default (6 x 7)")
            if (position == -1) return CheckResult(false, "Wrong prompt for board dimensions.")

            outputString = main.execute(input).trim()
            val (r, c) = if (input == "") listOf("6", "7") else input.lowercase().split("x").map{ it -> it.trim() }

            position = checkOutput(outputString.lowercase(), 0, "do you want to play single or multiple games?",
                "for a single game, input 1 or press enter", "input a number of games:")
            if (position == -1) return CheckResult(false, "Wrong prompt for multiple games.")

            outputString = main.execute("1").trim()
            position = checkOutput(outputString.lowercase(), 0, "anna vs joan",
                "$r x $c board", "single game")
            if (position == -1) return CheckResult(false, "Wrong game information output.")
            position = checkOutput(outputString.lowercase(), position,
                * getBoard(r.toInt(), c.toInt() ,whiteDiscs, blackDiscs))
            if (position == -1) return CheckResult(false, "Wrong board output.")
            main.stop()
        }

        return CheckResult.correct()
    }

    @DynamicTest
    fun cf19Test(): CheckResult {
        val whiteDiscs = mutableListOf<Pair<Int, Int>>()
        val blackDiscs = mutableListOf<Pair<Int, Int>>()

        val main = TestedProgram()
        var outputString = main.start().trim()

        var position = checkOutput(outputString.lowercase(), 0, "connect four")
        if (position == -1) return CheckResult(false, "Wrong program title.")
        position = checkOutput(outputString.lowercase(), position, "first player's name:")
        if (position == -1) return CheckResult(false, "Wrong prompt for first player's name.")

        outputString = main.execute("Anna").trim()
        position = checkOutput(outputString.lowercase(), 0, "second player's name:")
        if (position == -1) return CheckResult(false, "Wrong prompt for second player's name.")

        outputString = main.execute("Joan").trim()
        position = checkOutput(outputString.lowercase(), 0,
            "set the board dimensions (rows x columns)", "press enter for default (6 x 7)")
        if (position == -1) return CheckResult(false, "Wrong prompt for board dimensions.")

        outputString = main.execute("8x7").trim()
        position = checkOutput(outputString.lowercase(), 0, "do you want to play single or multiple games?",
            "for a single game, input 1 or press enter", "input a number of games:")
        if (position == -1) return CheckResult(false, "Wrong prompt for multiple games.")

        outputString = main.execute("").trim()
        position = checkOutput(outputString.lowercase(), 0, "anna vs joan",
            "8 x 7 board", "single game")
        if (position == -1) return CheckResult(false, "Wrong game information output, after Enter Pressed (single game).")
        position = checkOutput(outputString.lowercase(), position,
            * getBoard(8, 7 ,whiteDiscs, blackDiscs))
        if (position == -1) return CheckResult(false, "Wrong board output.")
        main.stop()

        return CheckResult.correct()
    }

    @DynamicTest
    fun cf20Test(): CheckResult {
        val whiteDiscs = mutableListOf<Pair<Int, Int>>()
        val blackDiscs = mutableListOf<Pair<Int, Int>>()

        val main = TestedProgram()
        var outputString = main.start().trim()

        var position = checkOutput(outputString.lowercase(), 0, "connect four")
        if (position == -1) return CheckResult(false, "Wrong program title.")
        position = checkOutput(outputString.lowercase(), position, "first player's name:")
        if (position == -1) return CheckResult(false, "Wrong prompt for first player's name.")

        outputString = main.execute("Anna").trim()
        position = checkOutput(outputString.lowercase(), 0, "second player's name:")
        if (position == -1) return CheckResult(false, "Wrong prompt for second player's name.")

        outputString = main.execute("Joan").trim()
        position = checkOutput(outputString.lowercase(), 0,
            "set the board dimensions (rows x columns)", "press enter for default (6 x 7)")
        if (position == -1) return CheckResult(false, "Wrong prompt for board dimensions.")

        outputString = main.execute("5X5").trim()
        position = checkOutput(outputString.lowercase(), 0, "do you want to play single or multiple games?",
            "for a single game, input 1 or press enter", "input a number of games:")
        if (position == -1) return CheckResult(false, "Wrong prompt for multiple games.")

        outputString = main.execute("1").trim()
        position = checkOutput(outputString.lowercase(), 0, "anna vs joan",
            "5 x 5 board", "single game")
        if (position == -1) return CheckResult(false, "Wrong game information output.")
        position = checkOutput(outputString.lowercase(), position,
            * getBoard(5, 5 ,whiteDiscs, blackDiscs))
        if (position == -1) return CheckResult(false, "Wrong board output.")
        position = checkOutput(outputString.lowercase(), position, "anna\'s turn")
        if (position == -1) return CheckResult(false, "Wrong prompt for player's turn.")

        var input = mutableListOf<String>("0", "6", "7", "10", "122")
        for (move in input) {
            outputString = main.execute(move).trim()
            position = checkOutput(outputString.lowercase(), 0, "the column number is out of range (1 - 5)",
                "anna\'s turn")
            if (position == -1) return CheckResult(false,"Wrong message for out of range input.")
        }

        input = mutableListOf<String>("one", "7i", "k", "12z")
        for (move in input) {
            outputString = main.execute(move).trim()
            position = checkOutput(outputString.lowercase(), 0, "incorrect column number",
                "anna\'s turn")
            if (position == -1) return CheckResult(false,"Wrong message for invalid column number.")
        }

        outputString = main.execute("end").trim()
        position = checkOutput(outputString.lowercase(), 0, "game over!")
        if (position == -1) return CheckResult(false, "Wrong \"Game over!\" message.")

        if (!main.isFinished) return CheckResult(false, "Program has not finished after end command")

        main.stop()
        return CheckResult.correct()
    }

    @DynamicTest
    fun cf21Test(): CheckResult {
        val whiteDiscs = mutableListOf<Pair<Int, Int>>()
        val blackDiscs = mutableListOf<Pair<Int, Int>>()

        val main = TestedProgram()
        var outputString = main.start().trim()

        var position = checkOutput(outputString.lowercase(), 0, "connect four")
        if (position == -1) return CheckResult(false, "Wrong program title.")
        position = checkOutput(outputString.lowercase(), position, "first player's name:")
        if (position == -1) return CheckResult(false, "Wrong prompt for first player's name.")

        outputString = main.execute("Anna").trim()
        position = checkOutput(outputString.lowercase(), 0, "second player's name:")
        if (position == -1) return CheckResult(false, "Wrong prompt for second player's name.")

        outputString = main.execute("Joan").trim()
        position = checkOutput(outputString.lowercase(), 0,
            "set the board dimensions (rows x columns)", "press enter for default (6 x 7)")
        if (position == -1) return CheckResult(false, "Wrong prompt for board dimensions.")

        outputString = main.execute("5X5").trim()
        position = checkOutput(outputString.lowercase(), 0, "do you want to play single or multiple games?",
            "for a single game, input 1 or press enter", "input a number of games:")
        if (position == -1) return CheckResult(false, "Wrong prompt for multiple games.")

        outputString = main.execute("3").trim()
        position = checkOutput(outputString.lowercase(), 0, "anna vs joan",
            "5 x 5 board", "total 3 games", "game #1")
        if (position == -1) return CheckResult(false, "Wrong game information output.")
        position = checkOutput(outputString.lowercase(), position,
            * getBoard(5, 5 ,whiteDiscs, blackDiscs))
        if (position == -1) return CheckResult(false, "Wrong board output.")
        position = checkOutput(outputString.lowercase(), position, "anna\'s turn")
        if (position == -1) return CheckResult(false, "Wrong prompt for player's turn.")

        val colHeight = MutableList(5) { 0 }
        var input = listOf<Int>(5, 2, 5, 3, 5, 4, 5)
        for ((index, move) in input.withIndex()) {
            outputString = main.execute(move.toString()).trim()
            colHeight[move - 1]++
            if ( index % 2 == 0 ) whiteDiscs.add(Pair(colHeight[move - 1], move)) else blackDiscs.add(Pair(colHeight[move - 1], move))
            position = checkOutput(outputString.lowercase(), 0,
                * getBoard(5, 5 ,whiteDiscs, blackDiscs))
            if (position == -1) return CheckResult(false, "Wrong board output.")
            val checkOutStr = if (index == 6) {
                colHeight.replaceAll { 0 }
                whiteDiscs.clear()
                blackDiscs.clear()
                arrayOf("player anna won", "score", "anna: 2 joan: 0", "game #2",
                    * getBoard(5, 5 ,whiteDiscs, blackDiscs), "joan\'s turn")
            }
            else if ( index % 2 == 1 ) arrayOf("anna\'s turn") else arrayOf("joan\'s turn")
            position = checkOutput(outputString.lowercase(), position, * checkOutStr)
            val errorStr = if (index == 6) "Wrong message after the end of a game of multiple games." else "Wrong prompt for player's turn."
            if (position == -1) return CheckResult(false, errorStr)
        }

        input = listOf<Int>(1, 1, 2, 2, 3, 3, 4)
        for ((index, move) in input.withIndex()) {
            outputString = main.execute(move.toString()).trim()
            colHeight[move - 1]++
            if ( index % 2 == 1 ) whiteDiscs.add(Pair(colHeight[move - 1], move)) else blackDiscs.add(Pair(colHeight[move - 1], move))
            position = checkOutput(outputString.lowercase(), 0,
                * getBoard(5, 5 ,whiteDiscs, blackDiscs))
            if (position == -1) return CheckResult(false, "Wrong board output.")
            val checkOutStr = if (index == 6) {
                colHeight.replaceAll { 0 }
                whiteDiscs.clear()
                blackDiscs.clear()
                arrayOf("player joan won", "score", "anna: 2 joan: 2", "game #3",
                    * getBoard(5, 5 ,whiteDiscs, blackDiscs), "anna\'s turn")
            }
            else if ( index % 2 == 0 ) arrayOf("anna\'s turn") else arrayOf("joan\'s turn")
            position = checkOutput(outputString.lowercase(), position, * checkOutStr)
            val errorStr = if (index == 6) "Wrong message after the end of a game of multiple games." else "Wrong prompt for player's turn."
            if (position == -1) return CheckResult(false, errorStr)
        }

        input = listOf<Int>(5, 1, 4, 1, 3, 1, 2)
        for ((index, move) in input.withIndex()) {
            outputString = main.execute(move.toString()).trim()
            colHeight[move - 1]++
            if ( index % 2 == 0 ) whiteDiscs.add(Pair(colHeight[move - 1], move)) else blackDiscs.add(Pair(colHeight[move - 1], move))
            position = checkOutput(outputString.lowercase(), 0,
                * getBoard(5, 5 ,whiteDiscs, blackDiscs))
            if (position == -1) return CheckResult(false, "Wrong board output.")
            val checkOutStr = if (index == 6) {
                colHeight.replaceAll { 0 }
                whiteDiscs.clear()
                blackDiscs.clear()
                arrayOf("player anna won", "score", "anna: 4 joan: 2", "game over!")
            }
            else if ( index % 2 == 1 ) arrayOf("anna\'s turn") else arrayOf("joan\'s turn")
            position = checkOutput(outputString.lowercase(), position, * checkOutStr)
            val errorStr = if (index == 6) "Wrong message after the end of a game of multiple games." else "Wrong prompt for player's turn."
            if (position == -1) return CheckResult(false, errorStr)
        }

        main.stop()
        return CheckResult.correct()
    }

    @DynamicTest
    fun cf22Test(): CheckResult {
        val whiteDiscs = mutableListOf<Pair<Int, Int>>()
        val blackDiscs = mutableListOf<Pair<Int, Int>>()

        val main = TestedProgram()
        var outputString = main.start().trim()

        var position = checkOutput(outputString.lowercase(), 0, "connect four")
        if (position == -1) return CheckResult(false, "Wrong program title.")
        position = checkOutput(outputString.lowercase(), position, "first player's name:")
        if (position == -1) return CheckResult(false, "Wrong prompt for first player's name.")

        outputString = main.execute("Anna").trim()
        position = checkOutput(outputString.lowercase(), 0, "second player's name:")
        if (position == -1) return CheckResult(false, "Wrong prompt for second player's name.")

        outputString = main.execute("Joan").trim()
        position = checkOutput(outputString.lowercase(), 0,
            "set the board dimensions (rows x columns)", "press enter for default (6 x 7)")
        if (position == -1) return CheckResult(false, "Wrong prompt for board dimensions.")

        outputString = main.execute("5X5").trim()
        position = checkOutput(outputString.lowercase(), 0, "do you want to play single or multiple games?",
            "for a single game, input 1 or press enter", "input a number of games:")
        if (position == -1) return CheckResult(false, "Wrong prompt for multiple games.")

        outputString = main.execute("3").trim()
        position = checkOutput(outputString.lowercase(), 0, "anna vs joan",
            "5 x 5 board", "total 3 games", "game #1")
        if (position == -1) return CheckResult(false, "Wrong game information output.")
        position = checkOutput(outputString.lowercase(), position,
            * getBoard(5, 5 ,whiteDiscs, blackDiscs))
        if (position == -1) return CheckResult(false, "Wrong board output.")
        position = checkOutput(outputString.lowercase(), position, "anna\'s turn")
        if (position == -1) return CheckResult(false, "Wrong prompt for player's turn.")

        val colHeight = MutableList(5) { 0 }
        var input = listOf<Int>(1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 2, 3, 4, 5, 2, 3, 4, 1, 5, 1)
        for ((index, move) in input.withIndex()) {
            outputString = main.execute(move.toString()).trim()
            colHeight[move - 1]++
            if ( index % 2 == 0 ) whiteDiscs.add(Pair(colHeight[move - 1], move)) else blackDiscs.add(Pair(colHeight[move - 1], move))
            position = checkOutput(outputString.lowercase(), 0,
                * getBoard(5, 5 ,whiteDiscs, blackDiscs))
            if (position == -1) return CheckResult(false, "Wrong board output.")
            val checkOutStr = if (index == 24) {
                colHeight.replaceAll { 0 }
                whiteDiscs.clear()
                blackDiscs.clear()
                arrayOf("it is a draw", "score", "anna: 1 joan: 1", "game #2",
                    * getBoard(5, 5 ,whiteDiscs, blackDiscs), "joan\'s turn")
            }
            else if ( index % 2 == 1 ) arrayOf("anna\'s turn") else arrayOf("joan\'s turn")
            position = checkOutput(outputString.lowercase(), position, * checkOutStr)
            val errorStr = if (index == 24) "Wrong message after the end of a game of multiple games." else "Wrong prompt for player's turn."
            if (position == -1) return CheckResult(false, errorStr)
        }

        input = listOf<Int>(1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 2, 3, 4, 5, 2, 3, 4, 1, 5, 1)
        for ((index, move) in input.withIndex()) {
            outputString = main.execute(move.toString()).trim()
            colHeight[move - 1]++
            if ( index % 2 == 1 ) whiteDiscs.add(Pair(colHeight[move - 1], move)) else blackDiscs.add(Pair(colHeight[move - 1], move))
            position = checkOutput(outputString.lowercase(), 0,
                * getBoard(5, 5 ,whiteDiscs, blackDiscs))
            if (position == -1) return CheckResult(false, "Wrong board output.")
            val checkOutStr = if (index == 24) {
                colHeight.replaceAll { 0 }
                whiteDiscs.clear()
                blackDiscs.clear()
                arrayOf("it is a draw", "score", "anna: 2 joan: 2", "game #3",
                    * getBoard(5, 5 ,whiteDiscs, blackDiscs), "anna\'s turn")
            }
            else if ( index % 2 == 0 ) arrayOf("anna\'s turn") else arrayOf("joan\'s turn")
            position = checkOutput(outputString.lowercase(), position, * checkOutStr)
            val errorStr = if (index == 24) "Wrong message after the end of a game of multiple games." else "Wrong prompt for player's turn."
            if (position == -1) return CheckResult(false, errorStr)
        }

        input = listOf<Int>(1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 2, 3, 4, 5, 2, 3, 4, 1, 5, 1)
        for ((index, move) in input.withIndex()) {
            outputString = main.execute(move.toString()).trim()
            colHeight[move - 1]++
            if ( index % 2 == 0 ) whiteDiscs.add(Pair(colHeight[move - 1], move)) else blackDiscs.add(Pair(colHeight[move - 1], move))
            position = checkOutput(outputString.lowercase(), 0,
                * getBoard(5, 5 ,whiteDiscs, blackDiscs))
            if (position == -1) return CheckResult(false, "Wrong board output.")
            val checkOutStr = if (index == 24) {
                colHeight.replaceAll { 0 }
                whiteDiscs.clear()
                blackDiscs.clear()
                arrayOf("it is a draw", "score", "anna: 3 joan: 3", "game over!")
            }
            else if ( index % 2 == 1 ) arrayOf("anna\'s turn") else arrayOf("joan\'s turn")
            position = checkOutput(outputString.lowercase(), position, * checkOutStr)
            val errorStr = if (index == 24) "Wrong message after the end of a game of multiple games." else "Wrong prompt for player's turn."
            if (position == -1) return CheckResult(false, errorStr)
        }

        main.stop()
        return CheckResult.correct()
    }

}

fun checkOutput(outputString: String, searchPos: Int, vararg checkStr: String): Int {
    var searchPosition = searchPos
    val isPlainText = outputString.contains('|') || outputString.contains('=')
    for (str in checkStr) {
        val newStr = if ( isPlainText ) str
            .replace('║', '|')
            .replace('╚', '=')
            .replace('═', '=')
            .replace('╩', '=')
            .replace('╝', '=')
        else str
        val findPosition = outputString.indexOf(newStr, searchPosition)
        if (findPosition == -1) return -1
        if ( outputString.substring(searchPosition until findPosition).isNotBlank() ) return -1
        searchPosition = findPosition + newStr.length
    }
    return searchPosition
}

fun getBoard(rows: Int, columns: Int, whiteDiscs: MutableList<Pair<Int, Int>>, blackDiscs: MutableList<Pair<Int, Int>>): Array<String> {
    val boardStr = StringBuilder()
    for (i in 1 until columns)
        boardStr.append("$i ")
    boardStr.appendLine("$columns")

    for (j in rows downTo 1) {
        for (i in 1..columns) {
            val strPos = if ( whiteDiscs.contains(Pair(j, i)) ) "║o" else if ( blackDiscs.contains(Pair(j, i)) ) "║*" else "║ "
            boardStr.append(strPos)
        }
        boardStr.appendLine("║")
    }

    boardStr.append("╚═")
    for (i in 1 until columns)
        boardStr.append("╩═")
    boardStr.appendLine("╝")

    return boardStr.toString().split("\n").toTypedArray()
}


