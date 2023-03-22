fun main() {    
    val numbers = readln().split(' ').map { it.toInt() }.toIntArray()
    // Do not touch lines above
    // Write only exchange actions here.

    val temp = numbers.first()
    numbers[0] = numbers[numbers.lastIndex]
    numbers[numbers.lastIndex] = temp

    // Do not touch lines below
    println(numbers.joinToString(separator = " "))
}