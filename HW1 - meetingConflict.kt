package org

// Given a list of meetings, determine whether any two meetings overlap.

// Meeting times will be pushed as a
// list of lists. The nested lists will contain the start and end
// times for each meeting. Meeting times will be formatted as decimals.

// Returns true if there is a conflict, false otherwise.

fun conflictCheck(times: List<List<Double>>): Boolean {
    val counts: MutableMap<Double, Int> = mutableMapOf()
    val countTranslate: MutableMap<Int, Int> = mutableMapOf(0 to 1, 1 to -1)
    for (m in 0..times.size - 1) {
        for (t in 0..1) {
            if (!counts.containsKey(times[m][t])) {
                counts[times[m][t]] = 0
            }
            counts[times[m][t]] = (counts[times[m][t]] ?: 0) + (countTranslate[t] ?: 0)
        }
    }
    val timeList: List<Double> = counts.keys.sorted()
    var count: Int = 0
    for (i in 0..timeList.size - 1) {
        count = count + (counts[timeList[i]] ?: 0)
        if (count > 1) {
            return true
        }
    }
    return false
}

fun main() {
    val test1: List<List<Double>> = listOf(listOf(10.11, 11.12), listOf(11.11, 12.11))
    val test2: List<List<Double>> = listOf(listOf(10.11, 11.12), listOf(11.13, 12.11))
    val test3: List<List<Double>> = listOf(listOf(10.11, 11.12), listOf(11.12, 12.11))
    val test4: List<List<Double>> = listOf(listOf(10.11, 11.12), listOf(11.12, 12.11), listOf(9.00, 10.11))
    val test5: List<List<Double>> = listOf(listOf(0.12, 1.42), listOf(0.54, 3.11), listOf(0.23, 0.54))
    val test6: List<List<Double>> = listOf(listOf(1.00, 2.00), listOf(1.00, 2.00))
    val test7: List<List<Double>> = listOf(listOf(1.00, 2.00), listOf(1.30, 2.00))
    val test8: List<List<Double>> = listOf(listOf(1.00, 2.00), listOf(1.30, 2.30))
    assert(conflictCheck(test1) == true)
    assert(conflictCheck(test2) == false)
    assert(conflictCheck(test3) == false)
    assert(conflictCheck(test4) == false)
    assert(conflictCheck(test5) == true)
    assert(conflictCheck(test6) == true)
    assert(conflictCheck(test7) == true)
    assert(conflictCheck(test8) == true)
    print("All tests pass")
}