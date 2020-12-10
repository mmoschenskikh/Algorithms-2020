package lesson4

import java.util.*

/**
 * Префиксное дерево для строк
 */
class KtTrie : AbstractMutableSet<String>(), MutableSet<String> {

    private class Node {
        val children: MutableMap<Char, Node> = linkedMapOf()
    }

    private var root = Node()

    override var size: Int = 0
        private set

    override fun clear() {
        root.children.clear()
        size = 0
    }

    private fun String.withZero() = this + 0.toChar()

    private fun findNode(element: String): Node? {
        var current = root
        for (char in element) {
            current = current.children[char] ?: return null
        }
        return current
    }

    override fun contains(element: String): Boolean =
        findNode(element.withZero()) != null

    override fun add(element: String): Boolean {
        var current = root
        var modified = false
        for (char in element.withZero()) {
            val child = current.children[char]
            if (child != null) {
                current = child
            } else {
                modified = true
                val newChild = Node()
                current.children[char] = newChild
                current = newChild
            }
        }
        if (modified) {
            size++
        }
        return modified
    }

    override fun remove(element: String): Boolean {
        val current = findNode(element) ?: return false
        return remove(current)
    }

    // трудоёмкость O(1), дополнительная память не требуется
    private fun remove(node: Node): Boolean {
        if (node.children.remove(0.toChar()) != null) {
            size--
            return true
        }
        return false
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: [java.util.Iterator] (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    override fun iterator(): MutableIterator<String> = TrieIterator()

    inner class TrieIterator internal constructor() : MutableIterator<String> {

        private val toVisit: Deque<Pair<Char, Node>> = LinkedList()
        private val branchingIndices: Deque<Int> = LinkedList()
        private val discovered = mutableSetOf<Node>()
        private val currentString = StringBuilder()
        private var stringsFound = 0

        init {
            for (child in root.children) {
                branchingIndices.addFirst(currentString.length)
                toVisit.addFirst(child.toPair())
            }
        }

        override fun hasNext(): Boolean = stringsFound < size

        override fun next(): String {
            if (!hasNext()) throw NoSuchElementException()
            if (branchingIndices.isNotEmpty() && currentString.isNotEmpty())
                currentString.delete(branchingIndices.removeFirst(), currentString.length - 1)
            do {
                val pair = toVisit.removeFirst()
                val char = pair.first
                val node = pair.second
                discovered.add(node)
                for (child in node.children.filter { it.value !in discovered }) {
                    toVisit.addFirst(child.toPair())
                }
                if (node.children.size > 1)
                    for (i in 1 until node.children.size)
                        branchingIndices.addFirst(currentString.length + 1)
                currentString.append(char)
            } while (char != 0.toChar())
            stringsFound++
            return currentString.filter { it != 0.toChar() }.toString()
        }

        override fun remove() {
            TODO("Not yet implemented")
        }
    }
}