fun main() {
    val report = readln()
    //write your code here.
    val regex = Regex("\\d wrong answers?")
    print(regex.matches(report))
}