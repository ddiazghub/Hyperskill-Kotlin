fun main() {
    val numbers = Array(readln().toInt()) { readln().toInt() }

    println(
        numbers
            .withIndex()
            .maxByOrNull { it.value }!!
            .index
    )
}