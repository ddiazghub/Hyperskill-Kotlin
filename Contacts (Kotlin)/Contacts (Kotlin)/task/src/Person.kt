package contacts

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

class Person(name: String, var surname: String) : Contact(name) {
    override val fullName: String
        get() = "$name $surname"

    var birthDate: LocalDate? = null

    var gender: String? = null
        set(value) {
            field = if (value != null && value.uppercase() in GENDERS) value
            else {
                println("Bad gender!")
                null
            }
        }

    fun setBirthDate(birthDate: String) {
        try {
            val date = birthDate.toLocalDate()
            this.birthDate = date
        } catch (e: IllegalArgumentException) {
            println("Bad birth date!")
            this.birthDate = null
        }
    }

    override fun editableFields(): String = "(name, surname, birth, gender, number)"

    override fun edit(field: String, value: String) {
        when (field) {
            "surname" -> surname = value
            "birth" -> setBirthDate(value)
            "gender" -> gender = value
            else -> super.edit(field, value)
        }

        this.timeLastEdit = Clock.System.now()
    }

    override fun toString(): String = """
        Name: $name
        Surname: $surname
        Birth date: ${birthDate ?: NO_DATA}
        Gender: ${gender ?: NO_DATA}
        Number: ${number ?: NO_DATA}
        Time created: $timeCreated
        Time last edit: $timeLastEdit
    """.trimIndent()

    companion object {
        private val PROMPTS = arrayOf(
            "Enter the name: ",
            "Enter the surname: "
        )

        val GENDERS = arrayOf("M", "F")

        fun read(): Person {
            val (name, surname) = PROMPTS.map { readln(it) }
            val contact = Person(name, surname)

            contact.setBirthDate(readln("Enter the birth date: "))
            contact.gender = readln("Enter the gender (M, F): ")
            contact.number = readln("Enter the number: ")

            return contact
        }
    }
}