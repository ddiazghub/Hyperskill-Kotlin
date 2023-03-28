package flashcards

import kotlin.io.path.Path
import kotlin.io.path.forEachLine
import kotlin.io.path.useLines
import kotlin.io.path.writeLines

class CardManager() {
    val terms: MutableMap<String, Card> = mutableMapOf()
    val definitions: MutableMap<String, Card> = mutableMapOf()
    val logs: MutableList<String> = mutableListOf()

    constructor(filename: String) : this() {
        this.import(filename)
    }

    fun println(message: Any) {
        this.logs.add(message.toString())
        kotlin.io.println(message)
    }

    fun readln(): String = kotlin.io.readln().let {
        logs.add(it)
        it
    }

    fun readln(prompt: String): String {
        this.println(prompt)

        return this.readln()
    }

    fun loopRead(prompt: String, stopCondition: (String) -> Boolean, block: (String) -> String) {
        while (true) {
            val input = this.readln(prompt)

            if (stopCondition(input))
                break

            this.println(block(input))
        }
    }

    fun add(term: String, definition: String, errors: Int = 0): Card {
        val card = Card(term, definition, errors)
        terms[card.term] = card
        definitions[card.definition] = card

        return card
    }

    fun add() {
        val term = this.readln("The card:")

        if (term in terms)
            throw CardException.CardExists(term)

        val definition = this.readln("The definition of the card:")

        if (definition in definitions)
            throw CardException.DefinitionExists(definition)

        this.println("The pair ${this.add(term, definition)} has been added.")
    }

    fun remove() {
        val term = this.readln("Which card?")

        if (term !in this.terms)
            throw CardException.NoSuchCard(term)

        definitions.remove(terms[term]!!.definition)
        terms.remove(term)
        this.println("The card has been removed.")
    }

    fun import(filename: String) {
        val file = Path(filename)
        var loaded = 0

        file.forEachLine {
            val (term, definition, errors) = it.split(':')
            this.add(term, definition, errors.toInt())
            loaded++
        }

        this.println("$loaded cards have been loaded.")
    }

    fun import() {
        val filename = this.readln("File name:")
        this.import(filename)
    }

    fun export(filename: String) {
        val file = Path(filename)
        val lines = this.terms.values
            .asSequence()
            .map { "${it.term}:${it.definition}:${it.errors}" }

        file.writeLines(lines)
        this.println("${this.terms.size} cards have been saved.")
    }

    fun export() {
        val filename = this.readln("File name:")
        this.export(filename)
    }

    fun ask() {
        val n = this.readln("How many times to ask?").toInt()

        repeat(n) {
            val card = this.terms.values.random()
            val definition = this.readln("Print the definition of \"${card.term}\":")

            val toPrint = if (definition == card.definition)
                "Correct!"
            else {
                card.errors++
                "Wrong. The right answer is \"${card.definition}\"." + if (definition in definitions) ", but your definition is correct for \"${definitions[definition]!!.term}\"." else ""
            }

            this.println(toPrint)
        }
    }

    fun log() {
        val file = Path(this.readln("File name:"))
        file.writeLines(this.logs)
        this.println("The log has been saved.")
    }

    fun hardest() {
        val maxErrors = this.terms.values.maxOfOrNull { it.errors } ?: return this.println("There are no cards with errors.")
        val hardest = this.terms.values.filter { it.errors == maxErrors }

        when (hardest.first().errors) {
            0 -> this.println("There are no cards with errors.")
            else -> when (hardest.size) {
                1 -> this.println("The hardest card is \"${hardest[0].term}\". You have $maxErrors errors answering it.")
                else -> this.println("The hardest cards are ${hardest.joinToString(", ") { "\"${it.term}\"" }}. You have $maxErrors errors answering them.")
            }
        }
    }

    fun resetStats() {
        for (card in this.terms.values) {
            card.errors = 0
        }

        this.println("Card statistics have been reset.")
    }
}