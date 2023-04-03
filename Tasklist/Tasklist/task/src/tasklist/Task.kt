package tasklist

import com.squareup.moshi.adapter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalTime
import java.time.format.DateTimeFormatter
import java.time.LocalTime
import java.time.format.ResolverStyle

class Task(var description: String, var priority: TaskPriority, var datetime: LocalDateTime) {
    val due: TaskDueTag
        get() = TaskDueTag.from(datetime)

    fun edit() {
        tryRead("Input a field to edit (priority, date, time, task):", "Invalid field") {
            when (it) {
                "priority" -> priority = readPriority()
                "date" -> datetime = LocalDateTime(readDate(), datetime.time)
                "time" -> datetime = LocalDateTime(datetime.date, readTime())
                "task" -> description = readDescription()
                else -> null
            }
        }
    }

    fun splitDescription(): Sequence<String> {
        return description.splitToSequence("\n").flatMap {
            it.chunkedSequence(44)
                .map { it + " ".repeat(44 - it.length) }
        }
    }

    fun toString(i: Int): String {
        return arrayOf(" ${if (i < 10) "$i " else "$i"} ", " ${datetime.date} ", " ${datetime.time} ", " $priority ", " $due ")
            .mapIndexed { index, it -> it.whitespaced(if (index in 3..4) 3 else it.length) }
            .plus(listOf(splitDescription()))
            .zipAll()
            .joinToString("\n") { "|${it.joinToString("|")}|" }
    }

    companion object {
        val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-M-d")
            .withResolverStyle(ResolverStyle.SMART)

        val TIME_FORMAT = DateTimeFormatter.ofPattern("H:m")
            .withResolverStyle(ResolverStyle.STRICT)

        fun read(): Task {
            val priority = readPriority()
            val date = readDate()
            val time = readTime()
            val description = readDescription()

            if (description.isEmpty())
                throw TasklistException.Blank

            return Task(description, priority, LocalDateTime(date, time))
        }

        fun readPriority(): TaskPriority = tryRead("Input the task priority (C, H, N, L):") { TaskPriority.parse(it) }
        fun readDate(): LocalDate = tryRead("Input the date (yyyy-mm-dd):", "The input date is invalid") { parseDate(it, DATE_FORMAT) }
        fun readTime(): kotlinx.datetime.LocalTime = tryRead("Input the time (hh:mm):", "The input time is invalid") { LocalTime.parse(it, TIME_FORMAT).toKotlinLocalTime() }
        fun readDescription(): String = readlns("Input a new task (enter a blank line to end):")
            .joinToString("\n") { it.trim() }
    }
}