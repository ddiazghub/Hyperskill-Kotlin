package flashcards

import java.io.IOException

fun Array<String>.parse(): Map<String, String?> {
    return this.asSequence()
        .windowed(2, partialWindows = true)
        .filter { it[0].startsWith('-') }
        .associate { it[0].substring(1) to if (it.getOrElse(1) { "-" }.startsWith('-')) null else it[1] }
}

fun main(args: Array<String>) {
    val argsMap = args.parse()
    val game = (argsMap["import"]?.let { CardManager(it) }) ?: CardManager()

    game.loopRead("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):", { it == "exit" }) {
        try {
            when (it) {
                "add" -> game.add()
                "remove" -> game.remove()
                "import" -> game.import()
                "export" -> game.export()
                "ask" -> game.ask()
                "log" -> game.log()
                "hardest card" -> game.hardest()
                "reset stats" -> game.resetStats()
            }

            ""
        } catch (e: CardException) {
            e.message!! + "\n"
        } catch (e: IOException) {
            "File not found.\n"
        }
    }

    println("Bye bye!")
    (argsMap["export"]?.let { game.export(it) })
}
