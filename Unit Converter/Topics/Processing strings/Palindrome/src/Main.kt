fun String.isPalindrome(): Boolean {
    return this.asSequence()
        .take(this.length / 2)
        .withIndex()
        .all { (i, char) -> char == this[this.lastIndex - i] }
}

fun main() {
    println(if (readln().isPalindrome()) "yes" else "no")
}