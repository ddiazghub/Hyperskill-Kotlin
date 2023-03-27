package wordsvirtuoso

import java.util.SortedSet
import kotlin.system.measureTimeMillis

class WordsVirtuoso(private val words: Dictionary, candidates: Dictionary) {
    private val secret: String = candidates.words.random()
    private val wrong: SortedSet<Char> = sortedSetOf()
    private var clues: MutableList<String> = mutableListOf()

    fun start() {
        println("Words Virtuoso")

        val elapsed = measureTimeMillis {
            while (true) {
                println("\nInput a 5-letter word:")

                when (val word = readln().uppercase()) {
                    "EXIT" -> return println("\nThe game is over.")
                    else -> when (val result = words.check(word)) {
                        CheckResult.Valid -> {
                            val clue = word.mapIndexed { i, char ->
                                when (secret.indexOf(char)) {
                                    -1 -> {
                                        this.wrong.add(char)
                                        "\u001B[48:5:7m$char\u001B[0m"
                                    }
                                    i -> "\u001B[48:5:10m$char\u001B[0m"
                                    else -> "\u001B[48:5:11m$char\u001B[0m"
                                }
                            }.joinToString("")

                            this.clues.add(clue)
                            println("\n${this.clues.joinToString("\n")}")

                            if (word == secret)
                                return@measureTimeMillis println("\nCorrect!")
                            else
                                println("\n\u001B[48:5:14m${this.wrong.joinToString("")}\u001B[0m")
                        }
                        else -> println(result.message)
                    }
                }
            }
        }

        when (val tries = this.clues.size) {
            1 -> println("Amazing luck! The solution was found at once.")
            else -> println("The solution was found after $tries tries in ${elapsed / 1000} seconds.")
        }
    }
}