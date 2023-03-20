fun main() {
    val answer = readln()
    val regex = Regex("I can('t)? do my homework on time!")

    println(regex.matches(answer))
}