fun summator(map: Map<Int, Int>): Int {
    return map.asSequence()
        .filter { (k, _) -> k % 2 == 0 }
        .sumOf { (k, v) -> v }
}