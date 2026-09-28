package org

/**
 * ``Graph`` represents a directed graph
 * @param VertexType the type that represents a vertex in the graph
 */
interface DGraph<VertexType> {
    // I think it was recommended that this function be added, even though it isn't
    // a part of the interface provided in the homework
    fun addVertex(v: VertexType)
    /**
     * @return the vertices in the graph
     */
    fun getVertices(): Set<VertexType>

    /**
     * Add an edge between [from] and [to] with edge weight [cost]
     */
    fun addEdge(from: VertexType, to: VertexType, cost: Double)

    /**
     * Get all the edges that begin at [from]
     * @return a map where each key represents a vertex connected to [from] and the value represents the edge weight.
     */
    fun getEdges(from: VertexType): Map<VertexType, Double>

    /**
     * Remove all edges and vertices from the graph
     */
    fun clear()
}

/**
 * ``MinPriorityQueue`` maintains a priority queue where the lower
 *  the priority value, the sooner the element will be removed from
 *  the queue.
 *  @param T the representation of the items in the queue
 */
interface MinPriorityQueue<T> {
    /**
     * @return true if the queue is empty, false otherwise
     */
    fun isEmpty(): Boolean

    /**
     * Add [elem] with at level [priority]
     */
    fun addWithPriority(elem: T, priority: Double)

    /**
     * Get the next (highest priority) element and remove this element from the queue.
     * @return the next element in terms of priority.  If empty, return null.
     */
    fun next(): T?

    /**
     * Adjust the priority of the given element
     * @param elem whose priority should change
     * @param newPriority the priority to use for the element
     *   the lower the priority the earlier the element int
     *   the order.
     */
    fun adjustPriority(elem: T, newPriority: Double)
}

/**
 * A class for the Graph class containing vertex and edge information for a network
 */
class Graph<VertexType>: DGraph<VertexType> {
    private val adjacencyList = mutableMapOf<VertexType, MutableMap<VertexType, Double>>()

    /**
     * Adds a vertex to the graph/adjacency list
     */
    override fun addVertex(v: VertexType) {
        if (v !in adjacencyList.keys) {
            adjacencyList[v] = mutableMapOf()
        }
    }

    /**
     * Gets the list of vertices within the graph
     * Outputs:
     *     A set containing all vertices in the graph
     */
    override fun getVertices(): Set<VertexType> {
        return adjacencyList.keys
    }

    /**
     * Adds a one-sided edge starting at the vertex `from` and ending at the vertex `to` with a weight of `cost`
     * Inputs:
     *     from: a vertex representing the start of the edge
     *     to: a vertex representing the end of the edge
     *     cost: a double representing the cost/weight of the edge
     */
    override fun addEdge(from: VertexType, to: VertexType, cost: Double) {
        addVertex(from)
        addVertex(to) // this should prevent issues if the user gives nonexistent vertices
        val adjacentVertices = adjacencyList[from]
        if (adjacentVertices != null && adjacencyList.contains(to)) {
            adjacentVertices[to] = cost
        }
    }

    /**
     * Gets a map of all the vertices and their associated costs the vertex `from` is the starting edge for
     * Inputs:
     *     from: a vertex representing the vertex edges will start from
     * Outputs:
     *     A map containing all the vertices `from` is the origin edge to and the associated cost
     */
    override fun getEdges(from: VertexType): Map<VertexType, Double> {
        // Note: Elvis operator gives us a value if left hand expression is null
        return adjacencyList[from] ?: mapOf()
    }

    /**
     * Clears all vertices and edges from graph
     */
    override fun clear() {
        adjacencyList.clear()
    }
}
/**
 * Unit tests for Graph
 */
fun graphUnitTests() {
    val graph = Graph<String>()
    assert(graph.getVertices().isEmpty())

    graph.addVertex("A")
    assert(graph.getVertices() == setOf("A"))
    assert(graph.getEdges("A").isEmpty())

    graph.addEdge("A", "B", 10.5)
    assert(graph.getVertices() == setOf("A", "B"))

    assert(graph.getEdges("A").size == 1)
    assert(graph.getEdges("A")["B"] == 10.567)
    assert(graph.getEdges("B").isEmpty())

    graph.addEdge("A", "C", 5.676767676767)
    assert(graph.getEdges("A").size == 2)
    assert(graph.getEdges("A")["C"] == 5.676767676767)

    assert(graph.getEdges("KJASFDHKJASHF").isEmpty())

    graph.clear()
    assert(graph.getVertices().isEmpty())
    assert(graph.getEdges("A").isEmpty())

    println("Graph class passes all tests!")
}

/**
 * A class for the Min-Heap data structure with public and private methods
 */
class MinHeap<T> {
    private var vertices: MutableList<Pair<T, Double>> = mutableListOf()
    private var indexMap: MutableMap<T, Int> = mutableMapOf()

    /**
     * @return true if the heap is empty and false otherwise
     */
    fun isEmpty(): Boolean {
        return vertices.isEmpty()
    }

    /**
     * Insert [data] into the heap with value [heapNumber]
     * @return true if [data] is added and false if [data] was already there
     */
    fun insert(data: T, heapNumber: Double):Boolean {
        if (contains(data)) {
            return false
        }
        vertices.add(Pair<T, Double>(data, heapNumber))
        indexMap[data] = vertices.size - 1
        percolateUp(vertices.size - 1)
        return true
    }

    /**
     * Gets the minimum value from the heap and removes it.
     * @return the minimum value in the heap (or null if heap is empty)
     */
    fun getMin(): T? {
        when (vertices.size) {
            0 -> {
                return null
            }
            1 -> {
                val tmp = vertices[0].first
                vertices = mutableListOf()
                return tmp
            }
            else -> {
                val tmp = vertices[0].first
                swap(0, vertices.size - 1)
                vertices.removeLast()
                indexMap.remove(tmp)
                bubbleDown(0)
                return tmp
            }
        }
    }

    /**
     * Change the number of an element
     * @param vertex the element to change
     * @param newNumber the new number for the element
     */
    fun adjustHeapNumber(vertex: T, newNumber: Double) {
        getIndex(of=vertex)?.also{ index ->
            vertices[index] = Pair(vertices[index].first, newNumber)
            // do both operations to avoid explicitly testing which way to go
            percolateUp(startIndex=index)
            bubbleDown(startIndex=index)
        }
    }

    /**
     * @return true if the element is in the heap, false otherwise
     */
    fun contains(vertex: T): Boolean {
        return getIndex(of=vertex) != null
    }

    /**
     * @return the index in the list where the element is stored (or null if
     *     not there)
     */
    private fun getIndex(of: T): Int? {
        return indexMap[of]
    }

    /**
     * Bubble down from [startIndex] if needed
     * @param startIndex the index in the tree to start the bubbling
     */
    private fun bubbleDown(startIndex: Int) {
        val startNumber = vertices[startIndex].second
        val leftIndex = getLeftIndex(of=startIndex)
        val rightIndex = getRightIndex(of=startIndex)
        val leftNumber = if (leftIndex >= vertices.size) null else vertices[leftIndex].second
        val rightNumber = if (rightIndex >= vertices.size) null else vertices[rightIndex].second

        /*
         * We determine whether we need to continue with bubbling
         * Case 1: for each child, either the number is less or the child doesn't exist
         * Case 2: either the right child doesn't exist (meaning the left child must) or
         *    the right child exists, the left child exists, and left is smaller than right
         * Case 3: this will capture the case where we need to swap to the right
         */
        if ((leftNumber == null || startNumber < leftNumber) &&
            (rightNumber == null || startNumber < rightNumber)) {
            return
        } else if (rightNumber == null || (leftNumber != null && leftNumber < rightNumber)) {
            // swap with left since it is smallest
            swap(leftIndex, startIndex)
            bubbleDown(leftIndex)
            return
        } else {
            // swap with right since it is smallest
            swap(rightIndex, startIndex)
            bubbleDown(rightIndex)
            return
        }
    }

    /**
     * Swap [index1] and [index2] in the tree
     * @param index1 the first element to swap
     * @param index2 the second element to swap
     */
    private fun swap(index1: Int, index2: Int) {
        // update our index map so we still can find thigns
        indexMap[vertices[index1].first] = index2
        indexMap[vertices[index2].first] = index1
        val tmp = vertices[index1]
        vertices[index1] = vertices[index2]
        vertices[index2] = tmp
    }

    /**
     * Percolate up from [startIndex] if needed
     * @param startIndex the index in the tree to start the percolation
     */
    private fun percolateUp(startIndex: Int) {
        val parentIndex = getParentIndex(of = startIndex)
        if (parentIndex < 0) {
            // we must be at the root
            return
        } else if (vertices[startIndex].second < vertices[parentIndex].second) {
            swap(parentIndex, startIndex)
            percolateUp(parentIndex)
        }
    }

    /**
     * Get the parent index in the list
     * @param of the index to start from
     * @return the index where the parent is stored (if applicable)
     */
    private fun getParentIndex(of: Int):Int {
        return (of - 1) / 2
    }

    /**
     * Get the left index in the list
     * @param of the index to start from
     * @return the index where the left child is stored (if applicable)
     */
    private fun getLeftIndex(of: Int):Int {
        return of * 2 + 1
    }

    /**
     * Get the right index in the list
     * @param of the index to start from
     * @return the index where the right child is stored (if applicable)
     */
    private fun getRightIndex(of: Int):Int {
        return of * 2 + 2
    }
}

/**
 * Unit tests for MinHeap
 */
fun minHeapUnitTests() {
    val heap = MinHeap<String>()
    assert(heap.isEmpty())

    assert(heap.insert("B", 10.1234))
    assert(!heap.isEmpty())
    assert(heap.contains("B"))
    assert(!heap.insert("B", 5.314))

    assert(heap.insert("A", 27.1534))
    assert(heap.insert("C", 5.12345))

    assert(heap.getMin() == "C")
    assert(heap.getMin() == "B")

    heap.insert("D", 32.12)
    heap.insert("E", 41.0)

    heap.adjustHeapNumber("E", 2.1354)
    assert(heap.getMin() == "E")
    assert(heap.getMin() == "A")
    assert(heap.getMin() == "D")

    assert(heap.getMin() == null)
    assert(heap.isEmpty())

    println("MinHeap class passes all tests!")
}

/**
 * A class for the Min Priority Queue data structure that heavily uses the `MinHeap` class
 */
class MPQ<T> : MinPriorityQueue<T> {
    private val queue = MinHeap<T>()

    /**
     * Checks if the queue is empty
     * Outputs:
     *     A boolean value representing whether the queue is empty
     */
    override fun isEmpty(): Boolean {
        return queue.isEmpty()
    }

    /**
     * Adds an element `elem` to the queue with respect to its priority value `priority`
     * Inputs:
     *     elem: the element to be added to the queue
     *     priority: a double representing the priority value of `elem`
     */
    override fun addWithPriority(elem: T, priority: Double) {
        queue.insert(elem, priority)
    }

    /**
     * Returns the minimum/front element of the queue and deletes it from the queue
     * Outputs:
     *     The min/front element of the queue
     */
    override fun next(): T? {
        return queue.getMin()
    }

    /**
     * Adjusts the priority value and position of `elem`, assigning it a new priority value `newPriority`
     * Inputs:
     *     elem: the element to be adjusted in the queue
     *     newPriority: the new priority value
     */
    override fun adjustPriority(elem: T, newPriority: Double) {
        queue.adjustHeapNumber(elem, newPriority)
    }
}

/**
 * Unit tests for MPQ
 */
fun MPQUnitTests() {
    val mpq = MPQ<String>()
    assert(mpq.isEmpty())

    mpq.addWithPriority("B", 20.354)
    assert(!mpq.isEmpty())

    mpq.addWithPriority("A", 10.1234)
    mpq.addWithPriority("C", 30.867)

    assert(mpq.next() == "A")
    assert(mpq.next() == "B")

    mpq.addWithPriority("D", 40.2354)

    mpq.adjustPriority("D", 5.3564)

    assert(mpq.next() == "D")
    assert(mpq.next() == "C")

    assert(mpq.next() == null)
    assert(mpq.isEmpty())

    println("MPQ class passes all tests!")
}

/**
 * Utilize Dijkstra's Algorithm to find the lowest cost path between two vertices
 * Inputs:
 *     graph: a network of type DGraph containing all vertices, edges, and costs
 *     start: a vertex within `graph` representing the starting/origin vertex
 *     end: a vertex withing `graph` representing the end/destination vertex
 * Outputs:
 *     If a path exists: A pair of a list containing vertices representing the lowest-cost path between
 *     `start` and `end` and a double representing the total cost of that path
 *     If no path exists: null if there is no valid path between `start` and `end`
 */
fun <VertexType> findShortestPath(graph: DGraph<VertexType>, start: VertexType, end: VertexType): Pair<List<VertexType>, Double>? {
    if (end == start) {
        return Pair(mutableListOf(start), 0.0)
    }
    val queue = MPQ<VertexType>()
    val prev = mutableMapOf<VertexType, VertexType>()
    val dist = mutableMapOf<VertexType, Double>()

    val notSource = graph.getVertices() - start
    for (v in notSource) {
        dist[v] = Double.POSITIVE_INFINITY
        queue.addWithPriority(v, Double.POSITIVE_INFINITY)
    }

    dist[start] = 0.0
    queue.addWithPriority(start, 0.0)

    while (!queue.isEmpty()) {
        val u = queue.next()
        for ((v, edgeCost) in graph.getEdges(u!!)) {
            val alt = dist[u]!! + edgeCost
            if (alt < dist[v]!!) {
                dist[v] = alt
                queue.adjustPriority(v, alt)
                prev[v] = u
            }
        }
    }
    if (prev[end] == null) {
        return null
    }
    var focus = end
    val path = mutableListOf(focus)
    while (prev[focus] != null) {
        path.addFirst(prev[focus]!!)
        focus = prev[focus]!!
    }

    val totalCost = dist[end]!!
    return Pair(path, totalCost)
}

// Sort of improper, but the final exercise sort of doubles as
// unit tests for findShortestPath if that was even a requirement.

/**
 * A function to execute the final exercise (the cities option) and unit tests
 */
fun main() {
    val cities = Graph<String>()
    cities.addVertex("Boston")
    cities.addVertex("Providence")
    cities.addVertex("New Haven")
    cities.addVertex("New York")
    cities.addVertex("Albany")
    cities.addVertex("Springfield")
    cities.addVertex("Worcester")
    cities.addVertex("Hartford")
    cities.addVertex("Danbury")
    cities.addVertex("Newark")

    cities.addEdge("Boston", "Providence", 48.9)
    cities.addEdge("Providence", "New Haven", 103.0)
    cities.addEdge("New Haven", "New York", 80.6)
    cities.addEdge("New York", "Albany", 152.0)
    cities.addEdge("Albany", "Springfield", 85.9)
    cities.addEdge("Springfield", "Worcester", 52.2)
    cities.addEdge("Worcester", "Boston", 43.9)
    cities.addEdge("Springfield", "Hartford", 26.0)
    cities.addEdge("Hartford", "New Haven", 38.9)
    cities.addEdge("Danbury", "New York", 67.6)
    cities.addEdge("Danbury", "New Haven", 35.8)
    cities.addEdge("Newark", "New York", 11.7)

    cities.addEdge("Providence", "Boston", 48.9)
    cities.addEdge("New Haven", "Providence", 103.0)
    cities.addEdge("New York", "New Haven", 80.6)
    cities.addEdge("Albany", "New York", 152.0)
    cities.addEdge("Springfield", "Albany", 85.9)
    cities.addEdge("Worcester", "Springfield", 52.2)
    cities.addEdge("Boston", "Worcester", 43.9)
    cities.addEdge("Hartford", "Springfield", 26.0)
    cities.addEdge("New Haven", "Hartford", 38.9)
    cities.addEdge("New York", "Danbury", 67.6)
    cities.addEdge("New Haven", "Danbury", 35.8)
    cities.addEdge("New York", "Newark", 11.7)

    println(findShortestPath(cities,"Hartford", "Albany"))
    println(findShortestPath(cities,"New York", "Boston"))
    println(findShortestPath(cities,"New York", "Springfield"))
    println(findShortestPath(cities,"Danbury", "Worcester"))
    println(findShortestPath(cities,"Newark", "Danbury"))
    println(findShortestPath(cities,"Danbury", "Danbury"))

    graphUnitTests()
    minHeapUnitTests()
    MPQUnitTests()
}