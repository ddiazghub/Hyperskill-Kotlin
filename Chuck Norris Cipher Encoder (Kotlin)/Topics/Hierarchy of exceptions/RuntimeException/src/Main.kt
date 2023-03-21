fun main() {
    try {
        "Epic fail".toInt()
    } catch(e: RuntimeException) {
        println("Well")
    } catch (e: Exception) {
        println("Wrong")
    }
}