@file:Suppress("UNUSED_PARAMETER")

package lesson7

import java.util.*

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
// трудоёмкость O(m * n), память O((m + 1) * (n + 1)), где m = first.length, n = second.length
fun longestCommonSubSequence(first: String, second: String): String {
    val height = first.length + 1
    val width = second.length + 1
    val lcs = Array(height) { IntArray(width) { 0 } }
    for (i in 1 until height) {
        for (j in 1 until width) {
            lcs[i][j] = when {
                first[i - 1] == second[j - 1] -> lcs[i - 1][j - 1] + 1
                else -> maxOf(lcs[i][j - 1], lcs[i - 1][j])
            }
        }
    }
    val answer = StringBuilder()
    fun collectLCS(i: Int, j: Int) {
        when {
            i == 0 || j == 0 -> return
            first[i - 1] == second[j - 1] -> {
                answer.append(first[i - 1])
                collectLCS(i - 1, j - 1)
            }
            lcs[i - 1][j] == lcs[i][j] -> collectLCS(i - 1, j)
            else -> collectLCS(i, j - 1)
        }
    }
    collectLCS(height - 1, width - 1)
    return answer.reverse().toString()
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */
// трудоёмкость O(n^2), память O(n)
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    val size = list.size
    if (size == 0) return list
    val prev = IntArray(size) { -1 }
    val localLength = IntArray(size) { 1 }
    var length = 0
    var pos = -1
    for (i in list.indices) {
        for (j in 0 until i) {
            if (list[j] < list[i] && localLength[j] + 1 > localLength[i]) {
                localLength[i] = localLength[j] + 1
                prev[i] = j
            }
        }
        if (localLength[i] > length) {
            length = localLength[i]
            pos = i
        }
    }
    val answer = LinkedList<Int>()
    while (pos != -1) {
        answer.addFirst(list[pos])
        pos = prev[pos]
    }
    return answer
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
fun shortestPathOnField(inputName: String): Int {
    TODO()
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5