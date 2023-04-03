package tasklist

import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

fun readln(prompt: String): String = println(prompt).let { readln() }

fun readlns(prompt: String): Sequence<String> = sequence {
    println(prompt)

    while (true) {
        readlnOrNull().takeIf { !it.isNullOrBlank() }
            ?.let { yield(it) }
            ?: break
    }
}

fun <T> tryRead(prompt: String, errorMessage: String? = null, transform: (String) -> T?): T {
    while (true) {
        try {
            return transform(readln(prompt))!!
        } catch (e: Exception) {
            errorMessage?.let { println(it) }
        }
    }
}

fun parseDate(date: String, format: DateTimeFormatter): LocalDate? {
    val d = java.time.LocalDate.parse(date, format)

    return if (!d.isLeapYear && d.month == Month.FEBRUARY && date.substring(date.length - 2).toIntOrNull() == 29) {
        null
    } else d.toKotlinLocalDate()
}

fun String.whitespaced(width: Int = this.length): Sequence<String> = sequence {
    yield(this@whitespaced)

    while (true)
        yield(" ".repeat(width))
}

fun <T> List<Sequence<T>>.zipAll(): Sequence<List<T>> = sequence {
    val iterators = this@zipAll.map { it.iterator() }

    while (iterators.all { it.hasNext() }) {
        yield(iterators.map { it.next() })
    }
}