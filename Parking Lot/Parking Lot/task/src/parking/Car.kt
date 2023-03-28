package parking

class Car(val plate: String, color: String) {
    val color = color.lowercase().replaceFirstChar { it.uppercaseChar() }

    init {
        if (!LP_REGEX.matches(plate))
            throw ParkingLotException.InvalidLicensePlate(plate)
    }

    companion object {
        val LP_REGEX = Regex("\\S+")
    }
}