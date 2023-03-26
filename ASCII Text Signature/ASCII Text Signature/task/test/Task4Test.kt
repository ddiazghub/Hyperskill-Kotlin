
import org.hyperskill.hstest.stage.StageTest
import org.hyperskill.hstest.testcase.CheckResult

private val allLetters = (('a'..'z') + ('A'..'Z')).joinToString("")

class Task4Test : StageTest<InputClue>() {

    override fun generate() = listOf(
            inputCase("Ian One\nVIP",
                    hint = "This test corresponds to the example #1."),

            inputCase("A b\nlong participant",
                    hint = "This test corresponds to the example #2."),

            inputCase("Bill Gates\nVIP"),
            inputCase("Tom Smith\nWorker"),
            inputCase("Mr Anonimous\nParticipant"),

            inputCase("X Y\nAbcdAbcdAbcdAbcd", true,
                    "This test checks a long status with even length."),

            inputCase("X Y\nAbcdAbcdAbcdAbcdA", true,
                    "This test checks a long status with uneven length."),

            inputCase("X Y\nStatus  with spaces",
                    hint = "Status should be printed with " +
                            "the same spaces as original, but " +
                            "name and surname shouldn't contain spaces."),

            inputCase("$allLetters Ivan\nHello", true,
                    hint = "This test checks all possible letters for name."),

            inputCase("Ivan Ivan\n$allLetters", true,
                    hint = "This test checks all possible letters for status.")
    )


    private inline fun checkBadgeBorder(badge: String, onFailure: (msg: String) -> Unit) {
        val lines = badge.split("\n")

        if (lines.map { it.length }.toSet().size != 1) {
            onFailure("Your border is not rectangular.")
            return
        }
        if (lines.size < 2) {
            onFailure("Your border is not rectangular.")
            return
        }
        if (lines.first().any { it != '8' } || lines.last() != lines.first()) {
            onFailure("Your top and bottom edges don't consist of '8'.")
            return
        }
        if (!lines.drop(1).dropLast(1).all { it.startsWith("88  ") && it.endsWith("  88") }) {
            onFailure("Your left and right edges don't consist of \"88\" with two spaces padding.")
            return
        }
    }

    /** Compare height, indentation and line contents. */
    private inline fun compareBadges(user: String, author: String, onFailure: (msg: String) -> Unit) {
        val userLines = user.split("\n")
        val authorLines = author.split("\n")

        if (userLines.size != authorLines.size) {
            onFailure("Signature height is incorrect: ${userLines.size} lines instead of ${authorLines.size}.")
            return
        }

        userLines.zip(authorLines)
                .forEach { (userStr, authorStr) ->
                    if (userStr.trim('8', ' ') != authorStr.trim('8', ' ')) {
                        onFailure("Some line in your signature is incorrect. " +
                                "Your output: \n\"$user\" \nCorrect output:\n\"$author\"")
                        return
                    } else if (userStr != authorStr) {
                        onFailure("Some indentation in your signature is incorrect. " +
                                "Your output: \n\"$user\" \nCorrect output:\n\"$author\"")
                        return
                    }
                }
    }

    override fun check(reply: String, clue: InputClue): CheckResult {
        val badgeStart = reply.indexOf('8')
        if (badgeStart == -1) {
            return CheckResult.wrong("Your output doesn't contain a signature, wrapped in '8' symbols.")
        }

        val userBadge = reply
                .substring(badgeStart)
                .trim('\n', ' ')

        checkBadgeBorder(userBadge) { errorMessage ->
            return CheckResult.wrong(errorMessage)
        }

        val badge = authors(clue.input)

        compareBadges(userBadge, badge) { errorMessage ->
            val (name, status) = clue.input.split("\n")
            return clue.toFailure("Wrong output for input lines \"$name\" and \"$status\". $errorMessage")
        }
        return CheckResult.correct()
    }
}
