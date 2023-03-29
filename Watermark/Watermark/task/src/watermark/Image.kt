package watermark

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

val VALID_BITCOUNTS = intArrayOf(24, 32)
val VALID_FORMATS = arrayOf("jpg", "png")
val COLOR_REGEX = Regex("[0-2]?[0-5]{1,2} [0-2]?[0-5]{1,2} [0-2]?[0-5]{1,2}")

val COLOR_COMPONENTS: Array<(Color) -> Int> = arrayOf(
    { it.red },
    { it.green },
    { it.blue }
)

fun image(filename: String, type: String): BufferedImage {
    val img = File(filename).let {
        if (it.exists()) ImageIO.read(it)
        else throw WatermarkException.NoSuchFile(filename)
    }

    return if (img.colorModel.numColorComponents != 3)
        throw WatermarkException.WrongColors(type)
    else if (img.colorModel.pixelSize !in VALID_BITCOUNTS)
        throw WatermarkException.WrongBitCount(type)
    else img
}

fun BufferedImage.grid(width: Int, height: Int): BufferedImage {
    val output = BufferedImage(width, height, this.type)

    for (i in 0 until width) {
        for (j in 0 until height) {
            output.setRGB(i, j, this.getRGB(i % this.width, j % this.height))
        }
    }

    return output
}

fun BufferedImage.combineWith(other: BufferedImage, x: Int, y: Int, transform: (Pair<Int, Int>) -> Int): BufferedImage {
    val output = BufferedImage(this.width, this.height, this.type)

    for (i in 0 until this.width) {
        for (j in 0 until this.height) {
            val color = if (i in x until x + other.width && j in y until y + other.height)
                transform(this.getRGB(i, j) to other.getRGB(i - x, j - y))
            else this.getRGB(i, j)

            output.setRGB(i, j, color)
        }
    }

    return output
}

fun Color.blendComponent(other: Color, weight: Int, component: (Color) -> Int): Int {
    return (weight * component(other) + (100 - weight) * component(this)) / 100
}

fun Color.blendWith(other: Color, weight: Int): Color {
    val (r, g, b) = COLOR_COMPONENTS.map { this.blendComponent(other, weight, it) }

    return Color(r, g, b)
}

fun BufferedImage.blendUnless(other: BufferedImage, x: Int, y: Int, weight: Int, predicate: (Pair<Int, Int>) -> Boolean): BufferedImage {
    return this.combineWith(other, x, y) { colors ->
        Color(colors.second).let {
            if (predicate(colors)) Color(colors.first)
            else Color(colors.first).blendWith(it, weight)
        }.rgb
    }
}

fun BufferedImage.blendWith(other: BufferedImage, x: Int, y: Int, weight: Int, transparent: Color?): BufferedImage {
    return this.blendUnless(other, x, y, weight) { Color(it.second) == transparent }
}

fun BufferedImage.blendWith(other: BufferedImage, x: Int, y: Int, weight: Int): BufferedImage {
    return this.blendUnless(other, x, y, weight) { Color(it.second, true).alpha == 0 }
}

fun parseColor(string: String): Color {
    return if (COLOR_REGEX.matches(string)) {
        val (r, g, b) = string.split(" ").map { it.toInt() }
        Color(r, g, b)
    } else throw WatermarkException.InvalidColor
}