package indigo

import kotlin.math.min

class Cards() {
    val cards: MutableList<String> = mutableListOf()

    val size: Int
        get() = this.cards.size

    constructor(deck: Cards, n: Int) : this() {
        this.add(deck, n)
    }

    fun take(n: Int = this.size): Sequence<String> = sequence {
        repeat(min(n, cards.size)) {
            yield(cards.removeLast())
        }
    }

    fun addAt(cards: Cards, i: Int): String = cards.takeAt(i).let {
        this.cards.add(it)
        it
    }

    fun takeAt(i: Int): String = this.cards.removeAt(i)
    fun add(cards: Cards, n: Int = cards.size) = this.cards.addAll(cards.take(n))
    fun top(): String? = this.cards.lastOrNull()
    fun shuffle() = cards.shuffle()
    override fun toString(): String = this.cards.joinToString(" ")

    companion object {
        val RANKS = arrayOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val SUITS = arrayOf("♦", "♥", "♠", "♣")

        val DEFAULT = RANKS
            .flatMap { rank -> SUITS.map { "$rank$it" } }
            .toMutableList()

        fun deck(): Cards {
            val deck = Cards()
            deck.cards.addAll(DEFAULT)
            deck.shuffle()

            return deck
        }

        fun cardCompare(card1: String, card2: String): Boolean {
            return card1.startsWith(card2.substring(0 until card2.lastIndex)) || card1.last() == card2.last()
        }
    }
}