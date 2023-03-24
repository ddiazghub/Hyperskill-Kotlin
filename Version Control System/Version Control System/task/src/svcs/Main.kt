package svcs

import java.nio.file.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.createFile
import kotlin.io.path.exists

fun main(args: Array<String>) {
    DIRS.asSequence()
        .filter { !it.exists() }
        .forEach(Path::createDirectory)

    FILES.asSequence()
        .filter { !it.exists() }
        .forEach(Path::createFile)

    val output = Commands.run(args)
    println(output)
}