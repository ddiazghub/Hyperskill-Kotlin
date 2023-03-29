package indigo

class AIPlayer(deck: Cards) : Player(deck) {
    override fun move(table: Cards): MoveResult {
        println(this.hand)

        if (this.hand.size == 1)
            return this.move(table, 0)

        if (table.size == 0)
            return this.defaultMove(table)

        val top = table.top()
        val candidates = this.candidates(top!!)

        return when (candidates.size) {
            0 -> this.defaultMove(table)
            1 -> this.move(table, candidates[0].index)
            else -> {
                val indexes = candidates.groupBy({ it.value.last().toString() }) { it.index }
                    .filter { it.value.size > 1 }.let {
                        it.ifEmpty {
                            candidates.groupBy({ it.value.substring(0 until it.value.lastIndex) }) { it.index }
                                .filter { it.value.size > 1 }.let {
                                    it.ifEmpty {
                                        mapOf("" to candidates.map { it.index })
                                    }
                            }
                        }
                }

                val picked = indexes.values.flatten().random()
                this.move(table, picked)
            }
        }
    }

    fun defaultMove(table: Cards): MoveResult {
        val indexes = groupSuits().filter { it.value.size > 1 }.let {
            it.ifEmpty {
                groupRanks().filter { it.value.size > 1 }.let {
                    it.ifEmpty {
                        mapOf("" to List(this.hand.size) { it })
                    }
                }
            }
        }

        val picked = indexes.values.flatten().random()
        return this.move(table, picked)
    }

    fun candidates(top: String): List<IndexedValue<String>> {
        return this.hand.cards.withIndex().filter {
            Cards.cardCompare(top, it.value)
        }
    }

    fun groupSuits(): Map<String, List<Int>> {
        return this.hand.cards.withIndex().groupBy({ it.value.last().toString() }) { it.index }
    }

    fun groupRanks(): Map<String, List<Int>> {
        return this.hand.cards.withIndex().groupBy({ it.value.substring(0 until it.value.lastIndex) }) { it.index }
    }
}