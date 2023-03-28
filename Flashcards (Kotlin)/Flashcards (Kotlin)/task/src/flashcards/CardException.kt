package flashcards

sealed class CardException(message: String) : RuntimeException(message) {
    class CardExists(term: String) : CardException("The card \"$term\" already exists.")
    class DefinitionExists(definition: String) : CardException("The definition \"$definition\" already exists.")
    class NoSuchCard(term: String) : CardException("Can't remove \"$term\": there is no such card.")
}
