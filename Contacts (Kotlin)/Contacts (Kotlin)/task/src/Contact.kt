package contacts

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

abstract class Contact(var name: String) {
    open val fullName: String
        get() = name

    var number: String? = null
        set(value) {
            if (!value.isNullOrBlank() && validatePhone(value))
                field = value
            else {
                field = null
                println("Wrong number format!")
            }
        }

    val timeCreated: Instant = Clock.System.now()
    var timeLastEdit: Instant = Clock.System.now()

    open fun matches(regex: Regex): Boolean = regex.containsMatchIn(fullName + (number ?: ""))

    open fun edit(field: String, value: String) {
        when (field) {
            "name" -> name = value
            "number" -> number = value
            else -> throw ContactException.NoSuchField(field)
        }
    }

    abstract fun editableFields(): String

    companion object {
        const val NO_DATA = "[no data]"
        val FIELDS = arrayOf("name", "surname", "number")
        val PHONE_REGEX = Regex("\\+?([a-zA-Z0-9]+[ -]?)?(\\([a-zA-Z0-9][a-zA-Z0-9]+\\)[ -]?)?([a-zA-Z0-9][a-zA-Z0-9]+)?([ -][a-zA-Z0-9][a-zA-Z0-9]+)*")

        fun validatePhone(phone: String): Boolean = PHONE_REGEX.matches(phone)
    }
}