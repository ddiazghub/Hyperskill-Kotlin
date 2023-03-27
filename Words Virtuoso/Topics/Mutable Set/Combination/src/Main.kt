fun solution(setSource: Set<String>, listSource: MutableList<String>): MutableSet<String> {
    val set = setSource.toMutableSet()
    set.addAll(listSource)

    return set
}