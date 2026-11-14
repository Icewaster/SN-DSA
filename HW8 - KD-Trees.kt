package org

import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.measureTime
import kotlin.time.DurationUnit

/**
 * A class implementing the KD Tree, which contains a Node class to represent each point.
 */
class KDTree(val k: Int) {
    /**
     * A class representing a single node within the KD Tree
     *
     * Inputs:
     *     point: a double array specifying the coordinates of the node
     *     axis: an integer specifying the axis that it has on the KD Tree (which axis of the coordinates will
     *     be looked at when searching and building the tree)
     *     left: a node representing the left (lesser node) of the node (null by default)
     *     right: a node representing the right (greater node) of the node (null be default)
     */
    class Node(val point: DoubleArray, val axis: Int, var left: Node? = null, var right: Node? = null)

    private var root: Node? = null

    /**
     * A function that constructs the KD tree given a list of coordinates.
     *
     * Inputs:
     *     coordsList: a list of double arrays equal in length that represent the coordinates that will fill the KD Tree
     */
    fun makeTree(coordsList: List<DoubleArray>) {
        for (coords in coordsList) {
            if (coords.size != k) {
                return
            }
        }
        root = fillTree(coordsList.map { it.clone() }, 0)
    }

    /**
     * A private helper function that deals with the actual tree filling by recursively calling itself to fill
     * branches. This constructs a point-based KD tree, where all nodes on the tree correspond to coordinates.
     *
     * Inputs:
     *     coordsList: a list of double arrays equal in length that represent the coordinates that will fill the KD Tree
     *     depth: an integer representing the depth/level of the node/coordinate within the tree
     *
     * Outputs:
     *     node: a node representing the root node of the section of the KD Tree.
     */
    private fun fillTree(coordsList: List<DoubleArray>, depth: Int): Node? {
        if (coordsList.isEmpty()) {
            return null
        }
        val axis = depth % k

        val sorted = coordsList.sortedBy { it[axis] }
        val medianIdx = sorted.size / 2
        val medianPoint = sorted[medianIdx]

        val node = Node(medianPoint, axis)

        node.left = fillTree(sorted.subList(0, medianIdx), axis + 1)
        node.right = fillTree(sorted.subList(medianIdx + 1, sorted.size), axis + 1)

        return node
    }

    /**
     * A function that utilizes the KD Tree to find the nearest coordinate in the KD Tree to an inputted coordinate
     *
     * Inputs:
     *     coord: a double array representing the coordinates we are trying to find the nearest neighbor of
     * Outputs:
     *     closestNode: a double array representing the closest point in the KD Tree to the inputting coordinates
     */
    fun nearestNeighbor(coord: DoubleArray): DoubleArray? {
        var closestNode: DoubleArray? = null
        var closestDist: Double = Double.MAX_VALUE

        /**
         * A helper function that executes the tree searching portion of the nearest-neighbor algorithm. It searches
         * one or both of the branches of a given node as needed, and constantly updates the closest node.
         *
         * Inputs:
         *     node: a node representing the node whose branches are to be searched
         */
        fun search(node: Node?) {
            if (node == null) {
                return
            }
            val point = node.point
            val axis = node.axis

            var dist = 0.0
            for (i in 0 until k) {
                val diff = coord[i] - point[i]
                dist = dist + diff * diff
            }

            if (dist < closestDist) {
                closestDist = dist
                closestNode = point
            }

            val second: Node?
            if (coord[axis] <= point[axis]) {
                second = node.right
                search(node.left)
            }
            else {
                second = node.left
                search(node.right)
            }
            val diff = coord[axis] - point[axis]
            if (diff * diff < closestDist) {
                search(second)
            }
        }
        search(root)
        return closestNode
    }
}

/**
 * A brute force nearest-neighbor search that finds the nearest neighbor to inputted coordinates in an
 * inputted list of coordinates
 *
 * Inputs:
 *     coordsList: a list of double arrays representing coordinates that can be potential neighbors
 *     coord: a double array representing the coordinates we are trying to find the nearest neighbor of
 * Outputs:
 *     closestNode: a double array representing the nearest neighbor to the inputted coordinates
 */
fun bruteForceNeighbor(coordsList: List<DoubleArray>, coord: DoubleArray): DoubleArray? {
    var closestNode: DoubleArray? = null
    var closestDist: Double = Double.MAX_VALUE

    for (i in coordsList.indices) {
        var dist = 0.0
        for (j in coord.indices) {
            val diff = coordsList[i][j] - coord[j]
            dist = dist + diff * diff
        }

        if (dist < closestDist) {
            closestDist = dist
            closestNode = coordsList[i]
        }
    }
    return closestNode
}

/**
 * Benchmark [KDTree] against brute force nearest neighbor.
 * 1000 test points will be generated to test against the training
 * points.
 *
 * @param k: the dimensionality of the dataset to create
 * @param numPoints: the number of points to use to match against
 *   (1000 test points will be used)
 * @return the triple of [Duration] objects where the first specifies
 * the time to build the tree, the second specifies the time it takes
 * to query the tree with 1,000 points, and the third is the time it
 * takes to find the nearest neighbor to these points using the brute
 * force approach.
 */
fun runExperiment(k: Int, numPoints: Int): Triple<Duration, Duration, Duration> {
    val coordsList = List(numPoints) { DoubleArray(k) { Random.nextDouble(0.0, 100.0) } }
    val testPoints = List(1000) { DoubleArray(k) { Random.nextDouble(0.0, 100.0) } }

    val tree = KDTree(k)

    val treeTime = measureTime { tree.makeTree(coordsList) }

    val KDNeighborTime = measureTime {
        for (point in testPoints) {
            tree.nearestNeighbor(point)
        }
    }

    val bruteForceNeighborTime = measureTime {
        for (point in testPoints) {
            bruteForceNeighbor(coordsList, point)
        }
    }

    return Triple(treeTime, KDNeighborTime, bruteForceNeighborTime)
}

fun main() {
    val kList = listOf(2, 4, 6, 8, 10, 12, 14, 16, 18, 20)
    val numList = listOf(10, 100, 1000, 10000)

    for (num in numList) {
        val totalTreeTime: MutableList<Double> = mutableListOf()
        val totalKDNeighborTime: MutableList<Double> = mutableListOf()
        val totalBruteForceNeighborTime: MutableList<Double> = mutableListOf()

        for (k in kList) {
            val (treeTime, KDNeighborTime, bruteForceNeighborTime) = runExperiment(k, num)
            totalTreeTime.add(treeTime.toDouble(DurationUnit.MILLISECONDS))
            totalKDNeighborTime.add(KDNeighborTime.toDouble(DurationUnit.MILLISECONDS))
            totalBruteForceNeighborTime.add(bruteForceNeighborTime.toDouble(DurationUnit.MILLISECONDS))
        }
        println("Total tree time: $totalTreeTime")
        println("Total KDNeighbor time: $totalKDNeighborTime")
        println("Total brute force: $totalBruteForceNeighborTime")
    }
}