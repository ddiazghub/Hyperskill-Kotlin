enum class Rainbow {
    RED,
    ORANGE,
    YELLOW,
    GREEN,
    BLUE,
    INDIGO,
    VIOLET,
    NULL;

    fun position(): Int = this.ordinal + 1

    companion object {
        fun fromName(colorName: String): Rainbow {
            return Rainbow.values()
                .asSequence()
                .find { value -> value.name.lowercase() == colorName.lowercase() }
                ?: Rainbow.NULL
        }
    }
}

fun main() {
    val color = readln()
    println(Rainbow.fromName(color).position())
}