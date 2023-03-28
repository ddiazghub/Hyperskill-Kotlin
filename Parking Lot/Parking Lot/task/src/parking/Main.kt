package parking

fun main() {
    var lot: ParkingLot? = null

    while (true) {
        try {
            val command = readln().split(' ')

            when (command[0]) {
                "create" -> {
                    lot = ParkingLot(command[1].toInt())
                    println("Created a parking lot with ${command[1]} spots.")
                }
                "status" -> lot?.status() ?: throw ParkingLotException.Null
                "park" -> Car(command[1], command[2]).let {
                    val spot = lot?.park(it) ?: throw ParkingLotException.Null
                    println("${it.color} car parked in spot $spot.")
                }
                "leave" -> {
                    lot?.leave(command[1].toInt()) ?: throw ParkingLotException.Null
                    println("Spot ${command[1]} is free.")
                }
                "reg_by_color" -> {
                    val query = lot?.regByColor(command[1]) ?: throw ParkingLotException.Null
                    println(query.joinToString(", "))
                }
                "spot_by_color" -> {
                    val query = lot?.spotByColor(command[1]) ?: throw ParkingLotException.Null
                    println(query.joinToString(", "))
                }
                "spot_by_reg" -> {
                    val query = lot?.spotByReg(command[1]) ?: throw ParkingLotException.Null
                    println(query)
                }
                "exit" -> return
            }
        } catch (e: ParkingLotException) {
            println(e.message)
        }
    }
}
