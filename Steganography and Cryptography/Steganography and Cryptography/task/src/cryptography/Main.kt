package cryptography

import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

const val FORMAT = "png"

val HIDE_PROMPTS = arrayOf(
    "Input image file:",
    "Output image file:",
    "Message to hide:",
    "Password:"
)

val SHOW_PROMPTS = arrayOf(
    "Input image file:",
    "Password:"
)

fun Array<String>.readAll(): List<String> = this.map {
    println(it)
    readln()
}

fun hide() {
    val (input, output, message, password) = HIDE_PROMPTS.readAll()
    val image = ImageIO.read(File(input))
    val outImage = image.hide(message, password)

    ImageIO.write(outImage, FORMAT, File(output))

    println("Input Image: $input")
    println("Output Image: $output")
    println("Message saved in $output image.")
}

fun show() {
    val (input, password) = SHOW_PROMPTS.readAll()
    val image = ImageIO.read(File(input))
    println("Message:")
    println(image.show(password))
}

fun main() {
    while (true) {
        println("Task (hide, show, exit):")

        try {
            when (val input = readln()) {
                "exit" -> break
                "hide" -> hide()
                "show" -> show()
                else -> println("Wrong task: $input")
            }
        } catch (e: IOException) {
            println("Can't read input file!")
        } catch (e: IndexOutOfBoundsException) {
            println(e.message)
        }
    }

    println("Bye!")
}