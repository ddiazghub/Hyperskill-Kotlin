package wordsvirtuoso

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun Path.lineSequence(): Sequence<String> = sequence { this@lineSequence.useLines { yieldAll(it) } }

class Dictionary(filepath: String) {
    val words: Set<String>

    init {
        val (valid, invalid) = Path(filepath)
            .lineSequence()
            .partition { Companion.check(it) == CheckResult.Valid }

        if (invalid.isNotEmpty())
            throw VirtuosoException.InvalidWords(filepath, invalid.size)

        words = valid
            .asSequence()
            .map { it.uppercase() }
            .toSet()
    }

    fun check(string: String): CheckResult {
        return when (val result = Companion.check(string)) {
            CheckResult.Valid -> if (string in words) result else CheckResult.InvalidWord
            else -> result
        }
    }

    companion object {
        fun check(string: String): CheckResult {
            if (string.length != 5)
                return CheckResult.WrongLength

            val chars = string.toSet()
            val unique = chars.asSequence()
                .filter { it in 'a'..'z' || it in 'A'..'Z' }
                .count()

            if (unique < chars.size)
                return CheckResult.InvalidChars

            if (unique < 5)
                return CheckResult.DuplicateChars

            return CheckResult.Valid
        }
    }
}