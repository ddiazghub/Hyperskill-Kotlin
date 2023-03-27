package sorting

import java.io.InputStream
import java.util.SortedMap

fun <T> InputStream.useAll(block: (Sequence<String>) -> T): T = this.bufferedReader().useLines(block)

fun <T: Comparable<T>> countFrequency(map: SortedMap<T, Int>, value: T): SortedMap<T, Int> {
    map[value] = (map[value] ?: 0) + 1
    return map
}

fun <T: Comparable<T>> InputStream.readLines(transform: (String) -> T): SortedMap<T, Int> {
    return this.useAll {
        it.map(transform)
            .fold(sortedMapOf(), ::countFrequency)
    }
}

fun <T: Comparable<T>> InputStream.readMany(transform: (String) -> Sequence<T>): SortedMap<T, Int> {
    return this.useAll {
        it.flatMap(transform)
            .fold(sortedMapOf(), ::countFrequency)
    }
}

fun InputStream.readInts(): SortedMap<Int, Int> = this.readMany {
    it.splitToSequence(WHITESPACE)
        .map {
            try { it.toInt() }
            catch (e: Exception) {
                println("\"$it\" is not a long. It will be skipped.")
                null
            }
        }
        .filterNotNull()
}

fun InputStream.readWords(): SortedMap<String, Int> = this.readMany { it.splitToSequence(WHITESPACE) }
fun InputStream.readLines(): SortedMap<String, Int> = this.readLines { "\n$it" }