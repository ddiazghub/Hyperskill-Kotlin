import kotlin.math.abs
import kotlin.math.sqrt

class Point(val x: Double, val y: Double) {
    fun norm(): Double = sqrt(this.x * this.x + this.y * this.y)

    fun delta(other: Point): Point {
        return Point(abs(this.x - other.x), abs(this.y - other.y))
    }

    fun distance(other: Point): Double = this.delta(other).norm()
}

fun perimeter(x1: Double, y1: Double, x2: Double, y2: Double, x3: Double, y3: Double, x4: Double = x3, y4: Double = y3): Double {
    val points = arrayOf(Point(x1, y1), Point(x2, y2), Point(x3, y3), Point(x4, y4))
    var perimeter = points[points.size - 1].distance(points[0])

    for (i in 0 until points.size - 1) {
        perimeter += points[i].distance(points[i + 1])
    }

    return perimeter
}