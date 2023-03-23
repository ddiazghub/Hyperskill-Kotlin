package cinema

sealed class CinemaException(message: String) : RuntimeException(message) {
    object ReservedSeat : CinemaException("That ticket has already been purchased!")
    object WrongInput : CinemaException("Wrong input!")
}

data class Statistics(val purchased: Int, val percentage: Double, val currentIncome: Int, val totalIncome: Int) {
    override fun toString(): String {
        return """
            Number of purchased tickets: $purchased
            Percentage: ${"%.2f".format(percentage)}%
            Current income: ${'$'}$currentIncome
            Total income: ${'$'}$totalIncome
        """.trimIndent()
    }
}

enum class Seat(private val symbol: String) {
    EMPTY("S"),
    RESERVED("B");

    override fun toString(): String = this.symbol
}

class Cinema(val rows: Int, val columns: Int) {
    private val seats: Array<Array<Seat>> = Array(this.rows) {
        Array(this.columns) { Seat.EMPTY }
    }

    private val totalSeats: Int = this.rows * this.columns
    private var reserved: Int = 0
    private var income: Int = 0

    private val totalIncome: Int = if (totalSeats > 60) {
        var half = totalSeats / 2
        half -= half % this.columns
        half * TICKET_PRICE + (totalSeats - half) * DISCOUNTED_PRICE
    } else totalSeats * TICKET_PRICE

    fun reserve(row: Int, column: Int): Int {
        if (row !in 0 until this.rows || column !in 0 until this.columns)
            throw CinemaException.WrongInput

        val price = if (totalSeats > 60 && row >= this.rows / 2) DISCOUNTED_PRICE else TICKET_PRICE

       if (this.seats[row][column] == Seat.RESERVED)
           throw CinemaException.ReservedSeat

        this.seats[row][column] = Seat.RESERVED
        this.reserved++
        this.income += price

        return price
    }

    fun statistics(): Statistics {
        val percentage = 100 * reserved.toDouble() / totalSeats

        return Statistics(reserved, percentage, income, totalIncome)
    }

    override fun toString(): String {
        val header = "  ${(1..this.columns).joinToString(" ")}"
        val body = seats.withIndex()
            .joinToString("\n") { (i, row) -> "${i + 1} ${row.joinToString(" ")}" }

        return "${header}\n${body}"
    }

    companion object {
        const val TICKET_PRICE = 10
        const val DISCOUNTED_PRICE = 8
    }
}

fun readInt(prompt: String): Int {
    println(prompt)

    return readln().toInt()
}

fun main() {
    val menu = """
        1. Show the seats
        2. Buy a ticket
        3. Statistics
        0. Exit
    """.trimIndent()

    val prompts = arrayOf(
        arrayOf("Enter the number of rows:", "Enter the number of seats in each row:"),
        arrayOf("Enter a row number:", "Enter a seat number in that row:")
    )

    val (rows, columns) = prompts[0].map(::readInt)
    val cinema = Cinema(rows, columns)

    while (true) {
        println()
        println(menu)
        val option = readln().toInt()
        println()

        when (option) {
            0 -> return
            1 -> println("Cinema:\n${cinema}")
            2 -> while (true) try {
                val (row, column) = prompts[1].map(::readInt)
                println("Ticket price: \$${cinema.reserve(row - 1, column - 1)}")
                break
            } catch (e: CinemaException) {
                println("\n${e.message}\n")
            }
            3 -> println(cinema.statistics())
        }

    }
}