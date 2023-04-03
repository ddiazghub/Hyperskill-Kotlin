package tasklist

import java.nio.file.Path
import kotlin.io.path.*

class Tasklist(filename: String) {
    val file: Path = Path(filename)

    val tasks: MutableList<Task> = if (file.exists()) {
        Json.adapter.fromJson(file.readText())!!
    } else mutableListOf()

    fun add() = tasks.add(Task.read())
    fun print() = println(this)

    fun select(): Int {
        print()

        val i = tryRead("Input the task number (1-${tasks.size}):", "Invalid task number") {
            it.toInt().takeIf { it in 1..tasks.size }
        }

        return i - 1
    }

    fun edit() {
        tasks[select()].edit()
        println("The task is changed")
    }

    fun delete() {
        tasks.removeAt(select())
        println("The task is deleted")
    }

    fun end() {
        if (!file.exists())
            file.createFile()

        file.writeText(Json.adapter.toJson(tasks))
        println("Tasklist exiting!")
    }

    override fun toString(): String {
        return if (tasks.isEmpty())
            throw TasklistException.Empty
        else {
            sequenceOf(TABLE_SEPARATOR, TABLE_HEADER, TABLE_SEPARATOR)
                .plus(tasks.asSequence().flatMapIndexed { i, task -> sequenceOf(task.toString(i + 1), TABLE_SEPARATOR) })
                .joinToString("\n") + "\n"
        }
    }

    companion object {
        const val TABLE_SEPARATOR = "+----+------------+-------+---+---+--------------------------------------------+"
        const val TABLE_HEADER = "| N  |    Date    | Time  | P | D |                   Task                     |"
    }
}