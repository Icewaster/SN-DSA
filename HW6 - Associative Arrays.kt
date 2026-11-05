package org

import com.sun.crypto.provider.ML_KEM_Impls

/**
 * Represents a mapping of keys to values.
 * @param K the type of the keys
 * @param V the type of the values
 */
interface AssociativeArray<K, V> {
    /**
     * Insert the mapping from the key, [k], to the value, [v].
     * If the key already maps to a value, replace the mapping.
     */
    operator fun set(k: K, v: V)

    /**
     * @return true if [k] is a key in the associative array
     */
    operator fun contains(k: K): Boolean

    /**
     * @return the value associated with the key [k] or null if it doesn't exist
     */
    operator fun get(k: K): V?

    /**
     * Remove the key, [k], from the associative array
     * @param k the key to remove
     * @return true if the item was successfully removed and false if the element was not found
     */
    fun remove(k: K): Boolean

    /**
     * @return the number of elements stored in the hash table
     */
    fun size(): Int

    /**
     * @return the full list of key value pairs for the associative array
     */
    fun keyValuePairs(): List<Pair<K, V>>
}

/**
 * A class implementing the association array/hash map, which contains AssociationList.
 * This would be the upper class.
 */
class HashMap<K, V> : AssociativeArray<K, V> {
    private var assoArray = List(53) { AssociationList<K, V>() }

    // Everytime the size of the list exceeds size * the load factor (0.75), the capacity
    // of the associative array increases to the next prime number described by the following list
    private val primeCapacities = listOf(
        53, 97, 193, 389, 769, 1543, 3079, 6151,
        12289, 24593, 49157, 98317, 196613, 393241, 786433
    )
    private var sz = 0 // The number of elements stored in the hash table
    private var primeInd = 0 // Just keeps track of what our current capacity is

    /**
     * Map the key [k] to the value [v], or remap [k] to [v] if [k] is already
     * mapped to a value.
     * Inputs:
     *     [k]: the key of type [K]
     *     [v]: the value of type [V] mapped to [k]
     */
    override fun set(k: K, v: V) {
        val hash = computeHash(k)
        val isNewKey = !contains(k)
        assoArray[hash][k] = v
        if (isNewKey) {
            sz = sz + 1
            checkAndExecuteReHash()
        }
    }

    /**
     * A helper function that compute the hash using the division-based method
     * Inputs:
     *     [k]: the key of type [K] to be hashed
     * Outputs:
     *     an integer representing the hash of [k]
     */
    private fun computeHash(k: K): Int {
        return Math.abs(k.hashCode()) % assoArray.size
    }

    /**
     * A helper function that checks whether the current size of the hashmap requires an increased
     * hashmap capacity. If a higher capacity is required, it will increase the
     */
    private fun checkAndExecuteReHash() {
        if (sz > primeCapacities[primeInd] * 0.75 && primeInd < primeCapacities.size - 1) {
            primeInd = primeInd + 1
            val pairs = keyValuePairs()
            assoArray = List(primeCapacities[primeInd]) { AssociationList<K, V>() }
            sz = 0
            for ((first, second) in pairs) {
                set(first, second)
            }
        }
    }

    /**
     * Checks whether the hashmap contains an entry with the key [k].
     * Inputs:
     *     [k]: a key of type [K] representing the key to be searched for
     * Outputs:
     *     A boolean value representing whether an entry with the key [k] was found
     */
    override fun contains(k: K): Boolean {
        val hash = computeHash(k)
        return assoArray[hash].contains(k)
    }

    /**
     * Gets the key value associated with the inputted value [k], or null if no key exists
     * Inputs:
     *     [k]: the key of type [K] of the key-value pair
     * Outputs:
     *     the value of type [V] mapped to [k], or null if no key-value pair contains [k]
     */
    override fun get(k: K): V? {
        val hash = computeHash(k)
        return assoArray[hash].get(k)
    }

    /**
     * Removes the key-value pair associated with [k] and returns a boolean representing
     * whether such a pair existed and was removed from the hashmap
     * Inputs:
     *     [k]: the key of type [K] representing the key-value pair to be removed
     * Outputs:
     *     A boolean representing whether there was a key-value containing [k] that was removed
     */
    override fun remove(k: K): Boolean {
        val hash = computeHash(k)
        val wasRemoved = assoArray[hash].remove(k)
        if (wasRemoved) {
            sz = sz - 1
        }
        return wasRemoved
    }

    /**
     * A function that returns the number of key-value pairs in the hashmap
     * Outputs:
     *     An integer representing the number of key-value pairs in the hashmap
     */
    override fun size(): Int {
        return sz
    }

    /**
     * A function that returns a list of pairs of all the key-value pairs in the hashmap
     * Outputs:
     *     allPairs: a list of pairs containing all of the key-value entries in the
     *     hashmap, where each pair contains a single key-value entry
     */
    override fun keyValuePairs(): List<Pair<K, V>> {
        val allPairs = mutableListOf<Pair<K, V>>()
        for (bucket in assoArray) {
            allPairs.addAll(bucket.keyValuePairs())
        }
        return allPairs
    }
}

/**
 * A class representing the association lists of the association array (a.k.a the buckets).
 */
class AssociationList<K, V>: AssociativeArray<K, V> {
    val list = DLL<Pair<K, V>>()
    private var sz = 0

    /**
     * Insert a key-value entry into the association list
     * Inputs:
     *     [k]: a key of type [K] representing the key of the key-value pair
     *     [v]: a value of type [V] mapped to [k]
     */
    override fun set(k: K, v: V) {
        val temp = DLL<Pair<K, V>>()
        var found = false

        while (!list.isEmpty()) {
            val pair = list.popFront()!!
            if (pair.first == k) {
                temp.pushBack(Pair(k, v))
                found = true
            }
            else {
                temp.pushBack(pair)
            }
        }

        while (!temp.isEmpty()) {
            list.pushBack(temp.popFront()!!)
        }
        if (!found) {
            list.pushFront(k to v)
            sz = sz + 1
        }
    }

    /**
     * Checks if a key is present in the association list.
     * Inputs:
     *     [k]: a key of type [K] representing the key to be checked for in the association list
     * Outputs:
     *     A boolean value representing whether a key-value pair containing
     *     [k] was present in the association list
     */
    override fun contains(k: K): Boolean {
        val temp = DLL<Pair<K, V>>()
        var found = false

        while (!list.isEmpty()) {
            val pair = list.popFront()!!
            if (pair.first == k) {
                found = true
            }
            temp.pushBack(pair)
        }

        while (!temp.isEmpty()) {
            list.pushBack(temp.popFront()!!)
        }

        return found
    }

    /**
     * A function that contains the value associated with [k], or null if
     * no such key exists in the association list
     * Inputs:
     *     [k]: the key of type [K] whose associated value is sought
     * Outputs:
     *     ret: the value mapped to [k], or null if [k]
     *     doesn't exist in the assocation list
     */
    override fun get(k: K): V? {
        val temp = DLL<Pair<K, V>>()
        var ret: V? = null

        while (!list.isEmpty()) {
            val pair = list.popFront()!!
            if (pair.first == k) {
                ret = pair.second
            }
            temp.pushBack(pair)
        }

        while (!temp.isEmpty()) {
            list.pushBack(temp.popFront()!!)
        }

        return ret
    }

    /**
     * Removes the key-value pair associated with [k] and returns a boolean representing
     * whether such a pair existed and was removed from the association list
     * Inputs:
     *     [k]: the key of type [K] representing the key-value pair to be removed
     * Outputs:
     *     A boolean representing whether there was a key-value containing [k] that was removed
     */
    override fun remove(k: K): Boolean {
        val temp = DLL<Pair<K, V>>()
        var isRemoved = false

        while (!list.isEmpty()) {
            val pair = list.popFront()!!
            if (pair.first != k) {
                temp.pushBack(pair)
            }
            else {
                isRemoved = true
                sz = sz - 1
            }
        }

        while (!temp.isEmpty()) {
            list.pushBack(temp.popFront()!!)
        }
        return isRemoved
    }

    /**
     * A function that returns the number of key-value pairs in the association list
     * Outputs:
     *     An integer representing the number of key-value pairs in the association list
     */
    override fun size(): Int {
        return sz
    }

    /**
     * A function that returns a list of pairs of all the key-value pairs in the association list
     * Outputs:
     *     allPairs: a list of pairs containing all of the key-value entries in the
     *     association list, where each pair contains a single key-value entry
     */
    override fun keyValuePairs(): List<Pair<K, V>> {
        val temp = DLL<Pair<K, V>>()
        val pairs = mutableListOf<Pair<K, V>>()

        while (!list.isEmpty()) {
            val pair = list.popFront()!!
            pairs.add(pair)
            temp.pushBack(pair)
        }

        while (!temp.isEmpty()) {
            list.pushBack(temp.popFront()!!)
        }
        return pairs
    }
}


fun hashMapUnitTests() {
    val hashMap = HashMap<Char, Int>()
    hashMap.set('A', 1)
    hashMap.set('B', 2)
    assert(hashMap.size() == 2)

    hashMap.remove('B')
    assert(hashMap.size() == 1)
    assert(hashMap.get('A') == 1)

    hashMap.set('B', 3)
    hashMap.set('B', 2)
    assert(hashMap.size() == 2)
    assert(hashMap.get('A') == 2)

    hashMap.set('C', 3)
    hashMap.set('D', 4)
    hashMap.set('E', 5)
    assert(hashMap.size() == 5)
    assert(!hashMap.contains('F'))
    assert(hashMap.contains('E'))

    assert(hashMap.keyValuePairs() == listOf('A' to 1, 'B' to 2, 'C' to 3, 'D' to 4, 'E' to 5))

    println("HashMap class passes all unit tests")
}


/**
 * A function that processes and compresses a string using the Lempel-Ziv algorithm
 * utilizing the HashMap class.
 *
 * Inputs:
 *     txt: the string that is to be processed
 * Outputs:
 *     encoded: a string represented the compressed version of [txt], consisting of binary
 *     hashes separated by spaces
 */
fun lempelZiv(txt: String): String {
    val legend = HashMap<String, Int>() // Will store all of our integer representations (A = 1, B = 2, AB = 3, etc.)

    // Assigning each of unique characters in txt an integer representation
    for (c in txt.toSet()) {
        legend.set(c.toString(), legend.size())
    }

    var prevSequence = ""
    var encoded = ""
    for (c in txt) {
        var currSequence = prevSequence + c // The current sequence we are looking at

        // Check if the current sequence has already been encountered
        if (legend.contains(currSequence)) {
            prevSequence = currSequence
        }
        // Add the cuurent sequence to our legend, assigning it a hash, and adding the
        // binary version of the hash to encoded
        else {
            encoded = encoded + legend[prevSequence]!!.toString(2) + " " // toString(2) converts to binary
            legend.set(currSequence, legend.size())
            prevSequence = c.toString()
        }
    }
    // This should add any remaining sequences that weren't already added
    if (prevSequence.isNotEmpty()) {
        encoded = encoded + legend[prevSequence]!!.toString(2)
    }
    return encoded // This should look something like 0 1 10 100 101 100 1
}

fun lempelZivOutputs() {
    print("ABBABBABABABABBA : ")
    println(lempelZiv("ABBABBABABABABBA"))

    print("ABCAACBCAA : ")
    println(lempelZiv("ABCAACBCAA"))

    print("QWERTYUIOP : ")
    println(lempelZiv("QWERTYUIOP"))

    print("TREESARECOOL : ")
    println(lempelZiv("TREESARECOOL"))

    print("FRIARFDIEFRRII : ")
    println(lempelZiv("FRIARFDIEFRRII"))
}

fun main() {
    hashMapUnitTests()
    lempelZivOutputs()
}