package org

interface LinkedList<T> {
    /**
     * Adds the element [data] to the front of the linked list.
     */
    fun pushFront(data: T)

    /**
     * Adds the element [data] to the back of the linked list.
     */
    fun pushBack(data: T)

    /**
     * Removes an element from the front of the list. If the list is empty, it is unchanged.
     * @return the value at the front of the list or nil if none exists
     */
    fun popFront(): T?

    /**
     * Removes an element from the back of the list. If the list is empty, it is unchanged.
     * @return the value at the back of the list or nil if none exists
     */
    fun popBack(): T?

    /**
     * @return the value at the front of the list or nil if none exists
     */
    fun peekFront(): T?

    /**
     * @return the value at the back of the list or nil if none exists
     */
    fun peekBack(): T?

    /**
     * @return true if the list is empty and false otherwise
     */
    fun isEmpty(): Boolean
}

class DLL<T>: LinkedList<T> {
    class Element<T>(var data: T, var next: Element<T>?, var prev: Element<T>?)
    private var head: Element<T>? = null
    private var tail: Element<T>? = null

    override fun pushFront(data: T) {
        val newHead = Element(data, head, null)
        head?.prev = newHead
        head = newHead

        if (tail == null) {
            tail = head
        }
    }

    override fun pushBack(data: T) {
        val newTail = Element(data, null, tail)
        tail?.next = newTail
        tail = newTail

        if (head == null) {
            head = tail
        }
    }

    override fun popFront(): T? {
        val ret = head?.data
        val newHead = head?.next
        newHead?.prev = null
        if (head == tail) {
            tail = null
        }
        head = newHead

        return ret
    }

    override fun popBack(): T? {
        val ret = tail?.data
        val newTail = tail?.prev
        newTail?.next = null
        if (head == tail) {
            head = null
        }
        tail = newTail

        return ret
    }

    override fun peekFront(): T? {
        return head?.data
    }

    override fun peekBack(): T? {
        return tail?.data
    }

    override fun isEmpty(): Boolean {
        return head == null
    }
}

fun DLLUnitTests() {
    val links = DLL<Int>()
    assert(links.isEmpty())

    links.pushFront(2)
    assert(links.peekFront() == 2)
    assert(links.peekBack() == 2)
    assert(!links.isEmpty())

    links.pushBack(7)
    assert(links.peekFront() == 2)
    assert(links.peekBack() == 7)

    links.pushBack(1); links.pushBack(8)
    assert(links.peekFront() == 8)

    links.popFront()
    assert(links.peekFront() == 7)

    links.popBack()
    assert(links.peekBack() == 1)

    assert(links.popFront() == 7)
    assert((links.peekBack() == links.peekFront()) && (links.peekFront() == 1))
    assert(links.popBack() == 1)
    assert(links.isEmpty())

    println("Doubly Linked List class passes all tests!")
}

// Exercise 1:
class Stack<T> {
    val stack = DLL<T>()

    fun push(data: T) {
        stack.pushFront(data)
    }

    fun pop(): T? {
        return stack.popFront()
    }

    fun peek(): T? {
        return stack.peekFront()
    }

    fun isEmpty(): Boolean {
        return stack.isEmpty()
    }
}

fun stackUnitTests() {
    val stack = Stack<Int>()
    assert(stack.isEmpty())

    stack.push(6)
    assert(stack.peek() == 6)
    assert(!stack.isEmpty())

    stack.push(5)
    assert(!stack.isEmpty())
    assert(stack.peek() == 5)

    stack.pop()
    assert(stack.peek() == 6)
    assert(stack.pop() == 6)

    println("Stack class passes all tests!")
}

// Exercise 2
class Queue<T> {
    val queue = DLL<T>()

    fun enqueue(data: T) {
        queue.pushFront(data)
    }

    fun dequeue(): T? {
        return queue.popBack()
    }

    fun peek(): T? {
        return queue.peekBack()
    }

    fun isEmpty(): Boolean {
        return queue.isEmpty()
    }
}

fun queueUnitTests() {
    val queue = Queue<Int>()
    assert(queue.isEmpty())

    queue.enqueue(1)
    queue.enqueue(2)
    assert(!queue.isEmpty())
    assert(queue.peek() == 2)

    queue.dequeue()
    assert(queue.peek() == 1)
    assert(queue.dequeue() == 1)

    println("Queue class passes all tests!")
}

// Exercise 3
fun <T> reverseStack(stack: Stack<T>): Stack<T> {
    val newStack: Stack<T> = Stack()
    while (!stack.isEmpty()) {
        newStack.push(stack.pop()!!)
    }
    return newStack
}

fun reverseStackUnitTests() {
    // I don't know if this is a requirement,
    // but it is a sanity test for me.
    val s: Stack<Int> = Stack()
    val sr: Stack<Int> = Stack()
    for (i in 1..10) {
        s.push(i)
    }
    for (i in 10 downTo 1) {
        s.push(i)
    }
    val t: Stack<Int> = Stack()
    t.push(4); t.push(1); t.push(6); t.push(1)
    val tr: Stack<Int> = Stack()
    tr.push(1); tr.push(6); tr.push(1); tr.push(4)
    assert(reverseStack(s) == sr)
    assert(reverseStack(t) == tr)
    println("reverseStack passes all tests!")
}

// Exercise 4
fun validParentheses(s: String): Boolean {
    val complement: Map<Char?, Char?> = mapOf('(' to ')', '{' to '}', '[' to ']')
    val stack = Stack<Char>()

    for (i in s) {
        if (!stack.isEmpty() && (i == complement[stack.peek()])) {
            stack.pop()
        }
        else {
            stack.push(i)
        }
    }
    return stack.isEmpty()
}

fun validParenthesisUnitTests() {
    assert(validParentheses("()"))
    assert(!validParentheses("("))
    assert(!validParentheses("(]"))
    assert(validParentheses("[()]"))
    assert(!validParentheses("([)]"))
    assert(!validParentheses("}"))
    assert(validParentheses("()[]{}{{()[()}}"))
    println("validParenthesis passes all tests!")
}

// Exercise 5
fun <T> copyStack(stack: Stack<T>): Stack<T> {
//    return reverseStack(reverseStack(stack)) I feel like you wanted me to use a queue
    val newStack = reverseStack(stack)
    val queue = Queue<T>()
    while (!newStack.isEmpty()) {
        queue.enqueue(newStack.pop()!!)
    }
    while (!queue.isEmpty()) {
        newStack.push(queue.dequeue()!!)
    }

    return newStack
}

fun copyStackUnitTests() {
    val s: Stack<Int> = Stack()
    for (i in 1..10) {
        s.push(i)
    }
    val t: Stack<Int> = Stack()
    val k: List<Int> = listOf(4, 1, 6, 1, 6, 9, 9, 2, 3)
    for (i in k) {
        t.push(i)
    }
    assert(copyStack(s) == s)
    assert(copyStack(t) == t)
    println("copyStack passes all tests!")
}

fun main() {
    DLLUnitTests()
    stackUnitTests()
    queueUnitTests()
    reverseStackUnitTests()
    validParenthesisUnitTests()
    copyStackUnitTests()
}