fun convertToStringList(list: List<Any>): List<String> {
    val stringList = mutableListOf<String>()

    for (element in list) {
        val stringElement = element as? String ?: "N/A"
        stringList.add(stringElement)
    }

    return stringList
}