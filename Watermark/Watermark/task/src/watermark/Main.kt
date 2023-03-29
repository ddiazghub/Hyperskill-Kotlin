package watermark

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun readln(prompt: String): String = print(prompt).let { readln() }

fun main() {
    try {
        val image = image(readln("Input the image filename: "), "image")
        val watermark = image(readln("Input the watermark image filename: "), "watermark")

        if (image.width < watermark.width || image.height < watermark.height)
            throw WatermarkException.WrongDimensions

        val (useAlpha, transparent) = if (watermark.transparency == BufferedImage.TRANSLUCENT) {
            (readln("Do you want to use the watermark's Alpha channel? ").lowercase() == "yes") to null
        } else if (readln("Do you want to set a transparency color? ").lowercase() == "yes") {
            val color = readln("Input a transparency color ([Red] [Green] [Blue]): ")
            false to parseColor(color)
        } else false to null

        val transparency = readln("Input the watermark transparency percentage (Integer 0-100): ").let {
            val value = it.toIntOrNull() ?: throw WatermarkException.InvalidTransparency
            value.takeIf { it in 0..100 } ?: throw WatermarkException.TransparencyOutOfRange
        }

        val maxX = image.width - watermark.width
        val maxY = image.height - watermark.height

        val (x, y, wm) = readln("Choose the position method (single, grid): ").let {
            when (it) {
                "grid" -> Triple(0, 0, watermark.grid(image.width, image.height))
                "single" -> readln("Input the watermark position ([x 0-$maxX] [y 0-$maxY]): ").let {
                    val (x, y) = it.split(" ")
                        .map { it.toIntOrNull() }
                        .filterNotNull()
                        .takeIf { it.size == 2 }
                        ?: throw WatermarkException.InvalidPosition

                    if (x in 0..maxX && y in 0..maxY)
                        Triple(x, y, watermark)
                    else throw WatermarkException.PositionOutOfRange
                }
                else -> throw WatermarkException.InvalidPositionMethod
            }
        }

        val (outputFile, format) = File(readln("Input the output image filename (jpg or png extension): ")).let {
            val extension = it.extension.lowercase()
            (it to extension).takeIf { extension in VALID_FORMATS } ?: throw WatermarkException.InvalidOutputFormat
        }

        val output = if (useAlpha) image.blendWith(wm, x, y, transparency)
        else image.blendWith(wm, x, y, transparency, transparent)

        ImageIO.write(output, format, outputFile)
        println("The watermarked image ${outputFile.path} has been created.")
    } catch (e: WatermarkException) {
        println(e.message)
    }
}