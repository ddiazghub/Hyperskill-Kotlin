package contacts

fun main() {
    val app = ContactList

    while (true) {
        try {
            when (readln("[menu] Enter action (add, list, search, count, exit): ")) {
                "add" -> app.add()
                "list" -> app.list()
                "search" -> app.search()
                "count" -> app.count()
                else -> return
            }
        } catch (e: ContactException) {
            println(e.message)
        }

        println()
    }
}