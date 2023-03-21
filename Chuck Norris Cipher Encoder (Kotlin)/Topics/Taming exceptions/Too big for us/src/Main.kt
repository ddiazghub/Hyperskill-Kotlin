fun returnValue(): Int {
    val value = readln().toInt()

    return if (value > 0) throw Exception("It's too big") else value
}