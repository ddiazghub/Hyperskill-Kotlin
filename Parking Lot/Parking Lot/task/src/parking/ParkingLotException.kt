package parking

sealed class ParkingLotException(message: String) : RuntimeException(message) {
    class InvalidLicensePlate(plate: String) : ParkingLotException("Invalid license plate: $plate")
    class NoCarInSpot(spot: Int) : ParkingLotException("There is no car in spot $spot.")
    class ColorNotFound(color: String) : ParkingLotException("No cars with color $color were found.")
    class LicensePlateNotFound(plate: String) : ParkingLotException("No cars with registration number $plate were found.")
    object Full : ParkingLotException("Sorry, the parking lot is full.")
    object Null : ParkingLotException("Sorry, a parking lot has not been created.")
}