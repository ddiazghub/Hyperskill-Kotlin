package search

import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readLines

class Search(filename: String) {
    val data: List<String> = Path(filename).let {
        if (it.exists()) it.readLines()
        else emptyList()
    }

    val indexes: Map<String, Set<Int>> = data.foldIndexed<String, MutableMap<String, MutableSet<Int>>>(mutableMapOf()) { i, indexes, line ->
        indexes.also {
            for (word in line.split(WHITESPACE))
                it.getOrPut(word.lowercase(), ::mutableSetOf).add(i)
        }
    }

    fun search(query: String, strategy: SearchStrategy): List<String> {
        return query.split(WHITESPACE).let {
            when (strategy) {
                SearchStrategy.Any -> searchAny(it)
                SearchStrategy.All -> searchAll(it)
                SearchStrategy.None -> searchNone(it)
            }
        }
    }

    private fun List<String>.searchMany(): List<Set<Int>> = this.map { indexes[it.lowercase()] ?: emptySet() }

    private fun searchAny(query: List<String>): List<String> {
        return query.searchMany()
            .reduce(Set<Int>::union)
            .map { data[it] }
    }

    private fun searchAll(query: List<String>): List<String> {
        return query.searchMany()
            .reduce(Set<Int>::intersect)
            .map { data[it] }
    }

    private fun searchNone(query: List<String>): List<String> {
        return query.searchMany()
            .reduce(Set<Int>::union)
            .let { data.filterIndexed { i, _ -> i !in it } }
    }

    companion object {
        val WHITESPACE = Regex("\\s+")
    }
}