package tasklist

import kotlinx.datetime.*

enum class TaskDueTag(val tag: String) {
    InTime("\u001B[102m \u001B[0m"),
    Today("\u001B[103m \u001B[0m"),
    Overdue("\u001B[101m \u001B[0m");

    override fun toString(): String = this.tag

    companion object {
        fun from(datetime: LocalDateTime): TaskDueTag = when (Clock.System.todayIn(TimeZone.UTC).daysUntil(datetime.date)) {
            0 -> Today
            in 1..Int.MAX_VALUE -> InTime
            else -> Overdue
        }
    }
}