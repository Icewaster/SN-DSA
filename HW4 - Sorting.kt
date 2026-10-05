package org

import kotlin.time.measureTime
import kotlin.random.Random
import kotlin.time.DurationUnit

/**
 * Bubble sort should have a time complexity of O(n^2) because it
 * has a nested loop that goes through the list
 *
 * Bubble sort, at least this implementation should have a space
 * complexity of O(n) because a copy of the list is created and returned
 */
fun bubbleSort(list: List<Int>): List<Int> {
    val returnList = list.toMutableList()
    var done = false
    while (!done) {
        done = true
        for (i in 0..returnList.size - 2) {
            if (returnList[i] > returnList[i + 1]) {
                val temp = returnList[i + 1]
                returnList[i + 1] = returnList[i]
                returnList[i] = temp
                done = false
            }
        }
    }
    return returnList
}

/**
 * Merge sort should have a time complexity of O(nlog(n)) because it goes through
 * log(n) lists and sorts them with n time complexity
 *
 * The space complexity should be O(n) because of the added lists in left, right, and result
 */
fun mergeSort(list: List<Int>): List<Int> {
    if (list.size <= 1) {
        return list
    }
    val split = list.size / 2
    val left = list.subList(0, split)
    val right = list.subList(split, list.size)

    val newLeft = mergeSort(left)
    val newRight = mergeSort(right)
    return merge(newLeft, newRight)
}

/**
 * Just a helper function
 */
fun merge(left: List<Int>, right: List<Int>): List<Int> {
    val result = mutableListOf<Int>()
    var i = 0
    var j = 0
    while (i < left.size && j < right.size) {
        if (left[i] > right[j]) {
            result.add(right[j])
            j += 1
        }
        else {
            result.add(left[i])
            i += 1
        }
    }

    for (k in i..left.size - 1) {
        result.add(left[k])
    }
    for (k in j..right.size - 1) {
        result.add(right[k])
    }

    return result
}

/**
 * Selection sort has a time complexity of O(n^2) because it contains a
 * nested for loop that iterates through the list
 *
 * Selection sort has a space complexity of O(1) because it only requires
 * a few internal helper variables that aren't collections of any sort
 */
fun selectionSort(list: List<Int>): List<Int> {
    val returnList = list.toMutableList()
    for (i in 0..returnList.size - 1) {
        var minInd = i
        for (j in i..returnList.size - 1) {
            if (returnList[j] < returnList[minInd]) {
                minInd = j
            }
        }
        val temp = returnList[i]
        returnList[i] = returnList[minInd]
        returnList[minInd] = temp
    }
    return returnList
}

/**
 * The time complexity of insertion sort should be O(n^2) because it has
 * a nested for loop that iterates every list entry.
 *
 * The space complexity should be O(n) because it creates a clone of the list
 * that it then changes to return
 */
fun insertionSort(list: List<Int>): List<Int> {
    val returnList = list.toMutableList()
    for (i in 1..returnList.size - 1) {
        var ind = i
        for (j in i downTo 0) {
            if (returnList[j] > returnList[ind]) {
                val temp = returnList[j]
                returnList[j] = returnList[ind]
                returnList[ind] = temp
                ind = j
            }
        }
    }
    return returnList
}

fun main() {
    val list1: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val ret1: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val list2: List<Int> = listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
    val ret2: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val list3: List<Int> = listOf(1, 2, 3, 4, 5, 4, 3, 2, 1)
    val ret3: List<Int> = listOf(1, 1, 2, 2, 3, 3, 4, 4, 5)
    val list4: List<Int> = listOf(5, 4, 3, 2, 1, 2, 3, 4, 5)
    val ret4: List<Int> = listOf(1, 2, 2, 3, 3, 4, 4, 5, 5)
    val list5: List<Int> = listOf(1, 1, 1, 1, 1, 1, 1, 1, 1)
    val ret5: List<Int> = listOf(1, 1, 1, 1, 1, 1, 1, 1, 1)
    val list6: List<Int> = listOf(1)
    val ret6: List<Int> = listOf(1)
    val list7: List<Int> = listOf(-1)
    val ret7: List<Int> = listOf(-1)
    val list8: List<Int> = listOf(-5, 3, 0, -2, 8, 3, -1)
    val ret8: List<Int> = listOf(-5, -2, -1, 0, 3, 3, 8)
    val list9: List<Int> = listOf(2, 1)
    val ret9: List<Int> = listOf(1, 2)
    val list10: List<Int> = listOf(-10, -5, 0, 5, 10)
    val ret10: List<Int> = listOf(-10, -5, 0, 5, 10)
    val list11: List<Int> = listOf(4, -2, 4, -2, 0, 0, 7)
    val ret11: List<Int> = listOf(-2, -2, 0, 0, 4, 4, 7)
    val lists: List<List<Int>> = listOf(list1, list2, list3, list4, list5, list6, list7, list8, list9, list10, list11)
    val rets: List<List<Int>> = listOf(ret1, ret2, ret3, ret4, ret5, ret6, ret7, ret8, ret9, ret10, ret11)

    for (i in rets.indices) {
        assert(bubbleSort(lists[i]) == rets[i])
        assert(mergeSort(lists[i]) == rets[i])
        assert(selectionSort(lists[i]) == rets[i])
        assert(insertionSort(lists[i]) == rets[i])
    }
    println("Sorting functions pass all unit tests.")

    val bubbleSortRunTimes = mutableListOf<Double>()
    for (size in listOf(10, 100, 1000, 10000, 100000)) {
        val x = (1 until size).map { Random.nextInt(100000) }
        val runTime = measureTime {
            bubbleSort(x)
        }
        bubbleSortRunTimes.add(runTime.toDouble(DurationUnit.SECONDS))
    }
    println("Bubble sort runtimes are $bubbleSortRunTimes")

    val mergeSortRunTimes = mutableListOf<Double>()
    for (size in listOf(10, 100, 1000, 10000, 100000)) {
        val x = (1 until size).map { Random.nextInt(100000) }
        val runTime = measureTime {
            mergeSort(x)
        }
        mergeSortRunTimes.add(runTime.toDouble(DurationUnit.SECONDS))
    }
    println("Merge sort runtimes are $mergeSortRunTimes")

    val selectionSortRunTimes = mutableListOf<Double>()
    for (size in listOf(10, 100, 1000, 10000, 100000)) {
        val x = (1 until size).map { Random.nextInt(100000) }
        val runTime = measureTime {
            bubbleSort(x)
        }
        selectionSortRunTimes.add(runTime.toDouble(DurationUnit.SECONDS))
    }
    println("Selection sort runtimes are $selectionSortRunTimes")

    val insertionSortRunTimes = mutableListOf<Double>()
    for (size in listOf(10, 100, 1000, 10000, 100000)) {
        val x = (1 until size).map { Random.nextInt(100000) }
        val runTime = measureTime {
            bubbleSort(x)
        }
        insertionSortRunTimes.add(runTime.toDouble(DurationUnit.SECONDS))
    }
    println("Insertion sort runtimes are $insertionSortRunTimes")
}