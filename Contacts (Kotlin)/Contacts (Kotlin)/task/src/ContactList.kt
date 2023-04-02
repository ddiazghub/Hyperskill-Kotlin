package contacts

object ContactList {
    val contacts: MutableList<Contact> = mutableListOf()

    fun add() {
        val contact = when (readln("Enter the type (person, organization): ")) {
            "person" -> Person.read()
            "organization" -> Organization.read()
            else -> throw ContactException.ContactType
        }

        this.contacts.add(contact)
        println("The record added.")
    }

    fun query(query: String? = null): List<Contact> {
        val found = query?.let {
            val regex = Regex(query, RegexOption.IGNORE_CASE)
            val found = contacts.filter { it.matches(regex) }

            when (val len = found.size) {
                1 -> println("Found 1 result:")
                else -> println("Found $len results:")
            }

            found
        } ?: contacts

        println(found.withIndex().joinToString("\n") {
            "${it.index + 1}. ${it.value.fullName}"
        } + "\n")

        return found
    }

    fun search() {
         val query = readln("Enter search query: ")
         val found = this.query(query)

        when (val action = readln("[search] Enter action ([number], back, again): ")) {
            "again" -> search()
            "back" -> return
            else -> record(found, this.toIndex(action, found.size))
        }
    }

    fun toIndex(index: String, size: Int = this.contacts.size): Int {
        val i = index.toIntOrNull()
            .takeIf { it in 1..size }
            ?: throw ContactException.IndexOutOfRange()

        return i - 1
    }

    fun record(records: List<Contact>, i: Int) {
        println(records[i])
        println()

        while (true) {
            when (readln("[record] Enter action (edit, delete, menu): ")) {
                "edit" -> edit(i)
                "delete" -> remove(i)
                else -> return
            }
        }
    }

    fun remove(i: Int) {
        this.contacts.removeAt(i)
        println("The record removed!")
    }

    fun edit(i: Int) {
        val contact = contacts[i]
        val field = readln("Select a field ${contact.editableFields()}: ")
        val value = readln("Enter $field: ")
        contact.edit(field, value)
        println("Saved")
        println(contact)
    }

    fun count() = println("The Phone Book has ${this.contacts.size} records.")

    fun list() {
        val contacts = this.query()

        when (val action = readln("[list] Enter action ([number], back): ")) {
            "back" -> return
            else -> record(contacts, this.toIndex(action))
        }
    }
}