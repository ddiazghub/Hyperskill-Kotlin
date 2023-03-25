fun swapInts(ints: IntArray) {
    val temp = ints[0]
    ints[0] = ints[1]
    ints[1] = temp
}

fun main() {
    val ints = IntArray(2) { readln().toInt() }
    swapInts(ints)
    ints.forEach(::println)
}