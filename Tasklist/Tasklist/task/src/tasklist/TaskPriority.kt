package tasklist

enum class TaskPriority(private val shortHand: String) {
    Critical("\u001B[101m \u001B[0m"),
    High("\u001B[103m \u001B[0m"),
    Normal("\u001B[102m \u001B[0m"),
    Low("\u001B[104m \u001B[0m");

    override fun toString(): String = this.shortHand

    companion object {
        fun parse(string: String): TaskPriority = when (string.uppercase()) {
            "C" -> Critical
            "H" -> High
            "N" -> Normal
            "L" -> Low
            else -> throw TasklistException.InvalidPriority
        }
    }
}