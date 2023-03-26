import org.hyperskill.hstest.testcase.CheckResult
import org.hyperskill.hstest.testcase.TestCase


/** Default testCase. */
fun <T> testCase(attach: T, input: String) = TestCase<T>().apply {
    setInput(input)
    setAttach(attach)
}

class InputClue(
        val input: String,
        val isPrivate: Boolean = false,
        /** Hint will be printed even for private tests. */
        val hint: String? = null
) {

    /** Ciphers [message] or adds a [hint] to the error if it is not null. */
    fun toFailure(message: String): CheckResult {
        if (isPrivate) {
            return CheckResult.wrong("Incorrect output. This is a private test. " + (hint ?: ""))
        } else {
            return CheckResult.wrong("$message ${hint ?: ""}")
        }
    }
}

fun inputCase(
        input: String,
        isPrivate: Boolean = false,
        hint: String? = null
) = testCase(InputClue(input, isPrivate, hint), input)


