package chess

fun main() {
    println("Pawns-Only Chess\nFirst Player's name:")
    val first = readln()
    println("Second Player's name:")
    val second = readln()
    val game = Chess(first, second)
    game.start()
}