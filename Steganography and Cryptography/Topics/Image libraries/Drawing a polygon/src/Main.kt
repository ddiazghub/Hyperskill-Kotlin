import java.awt.Color
import java.awt.Polygon
import java.awt.image.BufferedImage

val X = intArrayOf(50, 100, 200, 250, 200, 100)
val Y = intArrayOf(150, 250, 250, 150, 50, 50)

fun drawPolygon(): BufferedImage {
    val image = BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB)
    val graphics = image.createGraphics()
    graphics.color = Color.YELLOW
    graphics.drawPolygon(X, Y, X.size)

    return image
}