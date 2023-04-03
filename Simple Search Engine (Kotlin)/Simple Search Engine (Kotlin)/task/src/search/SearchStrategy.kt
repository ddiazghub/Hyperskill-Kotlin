package search

enum class SearchStrategy {
    All,
    Any,
    None;

    companion object {
        fun parse(string: String): SearchStrategy = when (string) {
            "ALL" -> All
            "ANY" -> Any
            "NONE" -> None
            else -> throw SearchException.InvalidSearchStrategy
        }
    }
}