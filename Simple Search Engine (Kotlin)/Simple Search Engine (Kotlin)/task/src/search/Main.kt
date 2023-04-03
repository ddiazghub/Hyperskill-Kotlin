package search

val MENU = "\n=== Menu ===\n1. Find a person\n2. Print all people\n0. Exit"

fun main(args: Array<String>) {
    val engine = Search(args.argValue("data") ?: "")

    while (true) {
        try {
            when (readln(MENU).toIntOrNull()) {
                0 -> return println("\nBye!")
                1 -> SearchStrategy.parse(readln("Select a matching strategy: ALL, ANY, NONE")).let {
                    val query = readln("\nEnter a name or email to search all matching people. ")

                    engine.search(query, it)
                        .takeIf { found -> found.isNotEmpty() }?.print()
                        ?: throw SearchException.NotFound
                }
                2 -> println("\n=== List of people ===").also { engine.data.print() }
                else -> throw SearchException.InvalidOption
            }
        } catch (e: SearchException) {
            println(e.message)
        }
    }
}