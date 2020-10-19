@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortTimes(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
// трудоёмкость O(n * log(n)), память O(n)
fun sortAddresses(inputName: String, outputName: String) {
    val regex = Regex("^[\\wА-ЯЁа-яё-]+ [\\wА-ЯЁа-яё-]+ - [\\wА-ЯЁа-яё-]+ \\d+\$")
    val addresses: MutableMap<String, MutableList<String>> = mutableMapOf()

    File(inputName).readLines().map { line ->
        if (!line.matches(regex))
            throw IllegalArgumentException("Wrong file format : $line")
        with(line.split(" - ")) {
            this.first() to this.last()
        }
    }.forEach {
        val address = it.second
        if (address !in addresses.keys) {
            addresses[address] = mutableListOf()
        }
        addresses[address]!!.add(it.first)
    }
    File(outputName).bufferedWriter().use { bw ->
        addresses.toList().sortedWith(
            compareBy(
                { it.first.split(" ").first() },
                { it.first.split(" ").last().toInt() }
            )
        ).forEach {
            bw.write("${it.first} - ${it.second.sorted().joinToString(separator = ", ")}")
            bw.newLine()
        }
    }
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */
// трудоёмкость O(n * log(n)), память O(n)
fun sortTemperatures(inputName: String, outputName: String) {
    val temperatures = File(inputName).readLines().map { it.toDouble() }
    require(temperatures.all { it in -273.0..500.0 })
    File(outputName).bufferedWriter().use { bw ->
        temperatures.sorted().forEach {
            bw.write(it.toString())
            bw.newLine()
        }
    }
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */
// трудоёмкость O(n + k), память O(n), где k = numbers.max()
@Suppress("DEPRECATION")
fun sortSequence(inputName: String, outputName: String) {
    val numbers = File(inputName).readLines().map { it.toInt() }.toIntArray()
    require(numbers.isNotEmpty())

    val sortedNumbers = countingSort(numbers, numbers.max()!!)
    var prevNumber = sortedNumbers.firstOrNull() ?: -1
    var count = 0
    var movingCount = 0
    var numberToMove = Int.MAX_VALUE

    for (number in sortedNumbers) {
        if (number == prevNumber) {
            count++
        }
        if (count > movingCount) {
            numberToMove = prevNumber
            movingCount = count
        } else if (count == movingCount) {
            numberToMove = minOf(prevNumber, numberToMove)
        }
        if (number != prevNumber) {
            prevNumber = number
            count = 1
        }
    }
    File(outputName).bufferedWriter().use { bw ->
        numbers.filter { it != numberToMove }.forEach {
            bw.write("$it")
            bw.newLine()
        }
        for (i in 1..movingCount) {
            bw.write("$numberToMove")
            bw.newLine()
        }
    }
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    TODO()
}

