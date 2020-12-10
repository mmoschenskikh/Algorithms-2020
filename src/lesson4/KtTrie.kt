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
        private val sizeAtStart = size
        private var prevBranching: Deque<Node> = LinkedList()
        private var current: Node? = null
        private var stringsFound = 0

        init {
            for (child in root.children) {
                branchingIndices.addFirst(currentString.length)
                toVisit.addFirst(child.toPair())
            }
        }

        // трудоёмкость O(1), дополнительная память не требуется
        override fun hasNext(): Boolean = stringsFound < sizeAtStart

        // трудоёмкость O(n) в худшем случае (удалены все элементы, кроме одного), память O(n + m) в худшем случае
        // где n - количество букв в дереве, m - длина самого длинного слова
        override fun next(): String {
            if (!hasNext()) throw NoSuchElementException()
            if (branchingIndices.isNotEmpty() && currentString.isNotEmpty())
                currentString.delete(branchingIndices.removeFirst(), currentString.length)
            if (prevBranching.isNotEmpty())
                current = prevBranching.removeFirst()
            do {
                val pair = toVisit.removeFirst()
                val char = pair.first
                val node = pair.second
                discovered.add(node)
                for (child in node.children.filter { it.value !in discovered }) {
                    toVisit.addFirst(child.toPair())
                }
                if (node.children.size > 1) {
                    for (i in 1 until node.children.size) {
                        prevBranching.addFirst(node)
                        branchingIndices.addFirst(currentString.length + 1)
                    }
                }
                if (char != 0.toChar()) {
                    currentString.append(char)
                    current = node
                    if (node.children.isEmpty()) {
                        currentString.delete(branchingIndices.removeFirst(), currentString.length)
                    }
                }
            } while (char != 0.toChar())
            stringsFound++
            return currentString.toString()
        }

        // трудоёмкость O(1), дополнительная память не требуется
        override fun remove() {
            if (current == null) throw IllegalStateException()
            remove(current!!)
            current = null
        }
    }
}
