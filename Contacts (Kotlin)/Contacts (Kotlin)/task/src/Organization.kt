package contacts

import kotlinx.datetime.Clock

class Organization(name: String, var address: String) : Contact(name) {
    override fun editableFields(): String = "(address, number)"

    override fun edit(field: String, value: String) {
        when (field) {
            "address" -> address = value
            else -> super.edit(field, value)
        }

        this.timeLastEdit = Clock.System.now()
    }

    override fun toString(): String = """
        Organization name: $name
        Address: $address
        Number: ${number ?: NO_DATA}
        Time created: $timeCreated
        Time last edit: $timeLastEdit
    """.trimIndent()

    companion object {
        private val PROMPTS = arrayOf(
            "Enter the organization name: ",
            "Enter the address: ",
            "Enter the number: "
        )

        fun read(): Organization {
            val (name, address, phone) = PROMPTS.map { readln(it) }
            val contact = Organization(name, address)
            contact.number = phone

            return contact
        }
    }

}