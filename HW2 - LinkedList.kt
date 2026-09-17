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

    /**
     * Push an item to the front of the linked list. Update head and tail accordingly.
     */
    override fun pushFront(data: T) {
        val newHead = Element(data, head, null)
        head?.prev = newHead
        head = newHead

        if (tail == null) {
            tail = head
        }
    }

    /**
     * Push an item to the back of the linked list. Update head and tail accordingly.
     */
    override fun pushBack(data: T) {
        val newTail = Element(data, null, tail)
        tail?.next = newTail
        tail = newTail

        if (head == null) {
            head = tail
        }
    }

    /**
     * Delete/unlink the front element of the list. Return the deleted element.
     */
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

    /**
     * Delete/unlink the back element of the list. Return the deleted element.
     */
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

    /**
     * Return the value/data of the front element
     */
    override fun peekFront(): T? {
        return head?.data
    }

    /**
     * Return the value/data of the back element
     */
    override fun peekBack(): T? {
        return tail?.data
    }

    /**
     * Return a boolean value of whether the list is empty
     */
    override fun isEmpty(): Boolean {
        return head == null
    }
}

/**
 * Unit tests for doubly linked list/DLL
 */
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
    assert(links.peekBack() == links.peekFront())
    assert((links.peekFront() == 1))
    assert(links.popBack() == 1)
    assert(links.isEmpty())

    println("Doubly Linked List class passes all tests!")
}

// Exercise 1:
class Stack<T> {
    val stack = DLL<T>()

    /**
     * Push an element to the top of the stack
     */
    fun push(data: T) {
        stack.pushFront(data)
    }

    /**
     * Remove the top element of the stack
     * Return the removed element
     */
    fun pop(): T? {
        return stack.popFront()
    }

    /**
     * Return the element at the top of the stack
     */
    fun peek(): T? {
        return stack.peekFront()
    }

    /**
     * Return a boolean value of whether the stack is empty
     */
    fun isEmpty(): Boolean {
        return stack.isEmpty()
    }
}

/**
 * Unit tests for Stack
 */
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

    /**
     * Push an element to the back of the queue
     */
    fun enqueue(data: T) {
        queue.pushBack(data)
    }

    /**
     * Remove an element from the front of the queue
     * Return the removed value
     */
    fun dequeue(): T? {
        return queue.popFront()
    }

    /**
     * Return the element at the front of the queue
     */
    fun peek(): T? {
        return queue.peekFront()
    }

    /**
     * Return a boolean value of whether the queue is empty
     */
    fun isEmpty(): Boolean {
        return queue.isEmpty()
    }
}

/**
 * Unit tests for Queue
 */
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
/**
 * A function that takes a stack a returns a
 * stack with its elements/contents reversed.
 */
fun <T> reverseStack(stack: Stack<T>): Stack<T> {
    val newStack: Stack<T> = Stack()
    while (!stack.isEmpty()) {
        newStack.push(stack.pop()!!)
    }
    return newStack
}

/**
 * Unit tests for reverseStack
 */
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
/**
 * A function that returns a boolean value of whether a
 * string of parenthesis, brackets, and curly brackets
 * are ordered correctly/properly
 */
fun validParentheses(s: String): Boolean {
    val complement: Map<Char?, Char?> = mapOf('(' to ')', '{' to '}', '[' to ']')
    val stack = Stack<Char>()

    for (i in s) {
        if (!stack.isEmpty() && (i == complement[stack.peek()])) {
            stack.pop() // pop top element of the stack if it is equal to its complement
        }
        else {
            stack.push(i)
        }
    }
    return stack.isEmpty() // valid cases should result in empty stacks
}

/**
 * Unit tests for validParenthesis
 */
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
/**
 * A function that creates/returns a copy of a stack, retaining
 * the inputted stack's elements and order.
 */
fun <T> copyStack(stack: Stack<T>): Stack<T> {
//    return reverseStack(reverseStack(stack)) I feel like you wanted me to use a queue
    val newStack = reverseStack(stack)
    val queue = Queue<T>()
    while (!newStack.isEmpty()) {
        queue.enqueue(newStack.pop()!!) // enqueue the popped element of newStack
    }
    while (!queue.isEmpty()) {
        newStack.push(queue.dequeue()!!) // push the dequeued element of the queue
    }

    return newStack
}

/**
 * Unit tests for copyStack
 */
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

/**
 * main function that runs unit tests
 */
fun main() {
    DLLUnitTests()
    stackUnitTests()
    queueUnitTests()
    reverseStackUnitTests()
    validParenthesisUnitTests()
    copyStackUnitTests()
}