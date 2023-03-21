try {
    throwException(data)
} catch (e: Exception) {
    handleException(data)
} finally {
    println("Will be executed in any case")
}