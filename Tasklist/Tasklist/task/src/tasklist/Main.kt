package tasklist

const val FILENAME = "tasklist.json"

fun main() {
    val tasks = Tasklist(FILENAME)

    while (true) {
        try {
            when (readln("Input an action (add, print, edit, delete, end):")) {
                "add" -> tasks.add()
                "print" -> tasks.print()
                "edit" -> tasks.edit()
                "delete" -> tasks.delete()
                "end" -> return tasks.end()
                else -> throw TasklistException.InvalidAction
            }
        } catch (e: TasklistException) {
            println(e.message)
        }
    }
}


