package wordsvirtuoso

enum class CheckResult(val message: String) {
    WrongLength("The input isn't a 5-letter word."),
    InvalidChars("One or more letters of the input aren't valid."),
    DuplicateChars("The input has duplicate letters."),
    InvalidWord("The input word isn't included in my words list."),
    Valid("The input is a valid string.")
}