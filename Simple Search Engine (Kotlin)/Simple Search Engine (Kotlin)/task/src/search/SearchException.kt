package search

sealed class SearchException(message: String) : RuntimeException(message) {
    object NotFound : SearchException("Not found")
    object InvalidOption : SearchException("Incorrect option! Try again.")
    object InvalidSearchStrategy : SearchException("Invalid search strategy.")
}