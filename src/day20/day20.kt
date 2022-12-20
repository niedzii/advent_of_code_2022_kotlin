package day20

import java.io.File
import kotlin.math.absoluteValue

fun main() {
    val input = File("src/day20", "day20_input.txt").readLines().map { it.toLong() }
    println("Stage 1 answer is ${stageOne(input)}") // 11073
    println("Stage 2 answer is ${stageTwo(input)}") // 11102539613040
}

fun stageOne(input: List<Long>): Long {
    val newList = input.mapIndexed { index, ele -> Element(index, ele, input.size - 1) }.toMutableList()

    (0 until newList.size).forEach { startIndex ->

        val element = newList.find { it.startIndex == startIndex }!!
        var index = newList.indexOf(element)


        repeat(element.value.absoluteValue.toInt()) {
            if (element.value > 0) {
                // move right
                index = move(newList, index, 1)
            } else {
                // move left
                index = move(newList, index, -1)
            }
        }
    }

    val indexOfZero = newList.indexOfFirst { it.value == 0L }

    return newList[(indexOfZero + 1000) % newList.size].value  + newList[(indexOfZero + 2000) % newList.size].value + newList[(indexOfZero + 3000) % newList.size].value
}

fun stageTwo(input: List<Long>): Long {
    val key = 811589153
    val newList = input.mapIndexed { index, ele -> Element(index, ele * key, input.size - 1 ) }.toMutableList()

    repeat(10) {
    (0 until newList.size).forEach { startIndex ->

        val element = newList.find { it.startIndex == startIndex }!!
        var index = newList.indexOf(element)


        repeat((element.moduledVal.absoluteValue).toInt()) {
            if (element.value > 0) {
                // move right
                index = move(newList, index, 1)
            } else {
                // move left
                index = move(newList, index, -1)
            }
        }
    }
    }

    val indexOfZero = newList.indexOfFirst { it.value == 0L }

    return newList[(indexOfZero + 1000) % newList.size].value  + newList[(indexOfZero + 2000) % newList.size].value + newList[(indexOfZero + 3000) % newList.size].value
}

class Element(
    var startIndex: Int,
    var value: Long,
    val elementsSize: Int,
    val moduledVal: Long = value % elementsSize
)

private fun move(list: MutableList<Element>, index: Int, direction: Int): Int {
    try {
        if(index == 1 && direction == -1) {
            // should be looped left
            val removed = list.removeAt(index)
            list.add(removed)
            return list.lastIndex
        } else if (index == list.size - 2 && direction == 1) {
            // should be looped  right
            val removed = list.removeAt(index)
            list.add(0, removed)
            return 0
        }

        val temp = list[index]
        list[index] = list[index + direction]
        list[index + direction] = temp

        return index + direction
    } catch (e: IndexOutOfBoundsException) {
        if(direction == -1) {
            // loop left
           val removed = list.removeAt(0)
            list.add(removed)
            return move(list, list.lastIndex, -1)
        } else {
            // loop right
            val removed = list.removeLast()
            list.add(0, removed)
            return  move(list, 0, 1)
        }
    }
}
