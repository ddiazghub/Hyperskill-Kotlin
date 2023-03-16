fun main() {
    var balance = readln().toInt()

    val items = readln()
        .split(' ')
        .asSequence()
        .map(String::toInt)

    for (item in items) {
        when {
            balance < item -> {
                println("Error, insufficient funds for the purchase. Your balance is $balance, but you need $item.")
                return
            }
            else -> balance -= item
        }
    }

    println("Thank you for choosing us to manage your account! Your balance is $balance.")
}