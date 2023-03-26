package signature

import kotlin.math.max

val MEDIUM = Font("medium.txt", 5)
val ROMAN = Font("roman.txt", 10)

fun nameTag(name: String, status: String): String {
    val widths = ROMAN.textWidth(name) to MEDIUM.textWidth(status)
    val len = max(widths.first, widths.second) + 4
    val border = "8".repeat(len + 4)
    val nameFormat = ROMAN.applyTo(name, "88", len)
    val statusFormat = MEDIUM.applyTo(status, "88", len)

    return "$border\n$nameFormat\n$statusFormat\n$border"
}

val PROMPTS = arrayOf(
    "Enter name and surname: ",
    "Enter person's status: "
)

fun main() {
    val (name, status) = PROMPTS.map {
        print(it)
        readln()
    }

    println(nameTag(name, status))
}
