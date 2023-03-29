package indigo

open class Player(deck: Cards) {
    val hand: Cards = Cards()
    var won: Int = 0
    var score: Int = 0

    init { deal(deck) }

    fun deal(deck: Cards) {
        if (hand.size == 0 && deck.size > 0)
            this.hand.add(deck, 6)
    }

    fun win(rename: Cards) {
        score += rename.take().count { card ->
            this.won++
            WINNER_RANKS.any { card.startsWith(it) }
        }
    }

    fun compare(other: Player, first: Boolean) {
        if (this.won > other.won)
            this.score += 3
        else if (this.won < other.won)
            other.score += 3
        else if (first)
            this.score += 3
        else other.score += 3
    }

    fun move(table: Cards, i: Int): MoveResult {
        val top = table.top() ?: "00"
        val card = table.addAt(hand, i)
        val isAI = this is AIPlayer

        if (isAI)
            println("Computer plays $card")

        return if (Cards.cardCompare(top, card)) {
            win(table)
            println("${if (isAI) "Computer" else "Player"} wins cards")
            MoveResult.Win
        } else MoveResult.Normal
    }

    open fun move(table: Cards): MoveResult {
        println(
            "Cards in hand: ${
                hand.cards.withIndex().joinToString(" ") { "${it.index + 1})${it.value}" }
            }"
        )

        var input: String

        do {
            input = readln("Choose a card to play (1-${hand.size}):")

            if (input == "exit")
                return MoveResult.Exit
        }
        while (input.toIntOrNull() !in 1..hand.size)

        return this.move(table, input.toInt() - 1)
    }

    companion object {
        val WINNER_RANKS = arrayOf("A", "10", "J", "Q", "K")
    }
}