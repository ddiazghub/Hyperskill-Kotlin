package parking

import java.lang.Integer.min

class ParkingLot(val size: Int) {
    val spots: Array<Car?> = Array(size) { null }
    var firstFree: Int = 0

    fun cars(): Sequence<Pair<Int, Car>> = sequence {
        spots.forEachIndexed { i, car ->
            if (car != null)
                yield(i to car)
        } }

    fun status() {
        val occupied = cars().toList()

        if (occupied.isEmpty())
            println("Parking lot is empty.")
        else
            println(occupied.joinToString("\n") { (i, car) ->
                "${i + 1} ${car!!.plate} ${car.color}"
            })
    }

    fun park(car: Car): Int {
        if (firstFree == size)
            throw ParkingLotException.Full

        spots[firstFree] = car
        val spot = firstFree + 1

        do firstFree++
        while (firstFree < spots.size && spots[firstFree] != null)

        return spot
    }

    fun leave(spotNumber: Int) {
        val spot = spotNumber - 1
        spots[spot] ?: throw ParkingLotException.NoCarInSpot(spotNumber)
        spots[spot] = null
        firstFree = min(firstFree, spot)
    }

    fun <T> query(get: (Pair<Int, Car>) -> T, by: (Pair<Int, Car>) -> Boolean, onFail: ParkingLotException): List<T> {
        val result = this.cars()
            .filter(by)
            .map(get)
            .toList()

        if (result.size == 0)
            throw onFail

        return result
    }

    fun regByColor(carColor: String): List<String> {
        val color = carColor.lowercase().replaceFirstChar { it.uppercaseChar() }

        return this.query({ it.second.plate }, { it.second.color == color }, ParkingLotException.ColorNotFound(carColor))
    }

    fun spotByColor(carColor: String): List<Int> {
        val color = carColor.lowercase().replaceFirstChar { it.uppercaseChar() }

        return this.query({ it.first + 1 }, { it.second.color == color }, ParkingLotException.ColorNotFound(carColor))
    }

    fun spotByReg(plate: String): Int {
        return this.query({ it.first + 1 }, { it.second.plate == plate }, ParkingLotException.LicensePlateNotFound(plate))[0]
    }
}