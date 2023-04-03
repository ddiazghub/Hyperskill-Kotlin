package search

fun readln(prompt: String): String = println(prompt).let { readln() }

fun Array<String>.argValue(arg: String): String? {
    val arg = "--$arg"

    return this.asSequence()
        .windowed(2)
        .find { it.getOrNull(0) == arg}
        ?.getOrNull(1)
}

fun <T> List<T>.print() = println(this.joinToString("\n"))