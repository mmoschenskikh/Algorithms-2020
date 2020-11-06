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

        private val elements: Queue<Pair<String, Node>> = LinkedList()
        private var current: Pair<String, Node>? = null

        init {
            // трудоёмкость O(n), память O(m * l),
            // где n - количество букв в дереве, m - количество слов в дереве, l - длина самого длинного слова
            traverse(root, "")
        }

        private fun traverse(node: Node, prefix: String): String {
            node.children.forEach { (ch, n) ->
                val string = traverse(n, prefix + ch)
                if (n.children.containsKey(0.toChar()))
                    elements.add(string to n)
            }
            return if (node.children.isEmpty()) prefix + 0.toChar() else prefix
        }

        // трудоёмкость O(1), дополнительная память не требуется
        override fun hasNext() = elements.isNotEmpty()

        // трудоёмкость O(1), дополнительная память не требуется
        override fun next(): String {
            current = elements.remove()
            return current!!.first
        }

        // трудоёмкость O(1), дополнительная память не требуется
        override fun remove() {
            if (current == null) throw IllegalStateException()
            remove(current!!.second)
            current = null
        }
    }
}