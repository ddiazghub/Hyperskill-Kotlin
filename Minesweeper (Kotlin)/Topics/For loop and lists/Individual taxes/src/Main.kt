data class Company(val income: Int, val taxRate: Int) {
    val taxRatio: Int = income * taxRate
}

fun main() {
    val n = readln().toInt()
    val incomes = Array(n) { readln().toInt() }

    val companies = incomes.map { income ->
        val taxRate = readln().toInt()
        Company(income, taxRate)
    }

    val max = companies
        .withIndex()
        .maxByOrNull { it.value.taxRatio }!!

    println(max.index + 1)
}