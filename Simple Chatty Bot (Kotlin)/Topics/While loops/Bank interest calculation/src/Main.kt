fun findYears(depo: Double): Int{
    var deposit = depo
    val interestRate = 1.071
    val max = 700000
    var years = 0

    while (deposit <= max) {
        deposit *= interestRate
        years++
    }

    return years
}