package org

// This is translated code from my LeetCode response for problem 739. The goal is to take in an array,
// temperatures, containing integers, and create an array answers, where answers[i]
// is the number of days from temperatures[i] to another day with a higher temperature.
// temperatures[i] must be in the range [30,100] and temperatures.size() in [1, 10^5].

// Python Code:
// def dailyTemperatures(self, temperatures: List[int]) -> List[int]:
// stack = []
// ret = [0] * len(temperatures)
// for i, j in enumerate(temperatures):
// while stack and temperatures[stack[-1]] < j:
// idx = stack.pop()
// ret[idx] = i - idx
// stack.append(i)
// return ret

// Translating the code didn't take too much time (setting up IntelliJ and Git did).
// Most of the challenge came from looking up Kotlin commands that I didn't know
// and unsimplifing some code that Python allows, but Kotlin doesn't.

fun dailyTemperatures(temperatures: List<Int>): MutableList<Int> {
    val stack = mutableListOf<Int>() // setting up the monotonic stack
    val ret = MutableList(temperatures.count()) {0} // list to be returned

    // iterate through each list element
    for (i in 0..temperatures.size - 1) {
        // if the current temperature is higher than the
        // temperature at the top of the stack, run
        while (stack.isNotEmpty() && temperatures[stack.last()] < temperatures[i]) {
            val idx = stack.last() // save the top stack element (index)
            stack.remove(stack.last()) // pop the last stack element
            ret[idx] = i - idx // calculate the number of days until idx's next hottest day
        }
        stack.add(i) // append the stack
    }
    return ret
}

fun main() {
    val test1 = listOf(73, 74, 75, 71, 69, 72, 76, 73)
    val answer1 = mutableListOf(1, 1, 4, 2, 1, 1, 0, 0)
    val test2 = listOf(30, 40, 50, 60)
    val answer2 = mutableListOf(1, 1, 1, 0)
    val test3 = listOf(30, 60, 90)
    val answer3 = mutableListOf(1, 1, 0)
    val test4 = listOf(31, 48, 42, 42, 42, 42, 55, 54, 99)
    val answer4 = mutableListOf(1, 5, 4, 3, 2, 1, 2, 1, 0)

    assert(dailyTemperatures(test1) == answer1)
    assert(dailyTemperatures(test2) == answer2)
    assert(dailyTemperatures(test3) == answer3)
    assert(dailyTemperatures(test4) == answer4)
    println("All tests pass!")
}