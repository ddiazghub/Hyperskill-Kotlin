package wordsvirtuoso

sealed class VirtuosoException(message: String) : RuntimeException(message) {
    object WrongArgs : VirtuosoException("Wrong number of arguments.")
    class NoSuchWordsFile(filename: String) : VirtuosoException("The words file $filename doesn't exist.")
    class NoSuchCandidatesFile(filename: String) : VirtuosoException("The candidate words file $filename doesn't exist.")
    class InvalidWords(filename: String, number: Int): VirtuosoException("$number invalid words were found in the $filename file.")
    class CandidatesNotInWords(wordsFilename: String, number: Int): VirtuosoException("$number candidate words are not included in the $wordsFilename file.")
}