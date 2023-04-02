package contacts

sealed class ContactException(message: String) : RuntimeException(message) {
    class NoRecords(action: String) : ContactException("No records to $action!")
    class NoSuchField(field: String) : ContactException("Field $field does not exist.")
    class IndexOutOfRange : ContactException("Contact index out of range")

    object ContactType: ContactException("Unknown contact type")
}