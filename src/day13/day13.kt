package day13

import com.beust.klaxon.Klaxon
import java.io.File
import kotlin.math.min

fun main() {
    val input = File("src/day13", "day13_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 5882
    println("Stage 2 answer is ${stageTwo(input)}") // 24948
}

fun stageOne(input: List<String>): Int {
    var result = 0
    input.map { "{\"wrapping\":$it}" }.windowed(3, 3, true).map { it[0].parseLine() to it[1].parseLine() }
        .map { it.compare() }.forEachIndexed { index, decision ->
        if (decision == Decision.RIGHT) result += index + 1
    }
    return result
}

fun stageTwo(input: List<String>): Int {
    val parsed: MutableList<Any> =
        input.toMutableList().apply { add("[[2]]") }.apply { add("[[6]]") }.filterNot { it.isBlank() }
            .map { "{\"wrapping\":$it}" }.map { it.parseLine() }.toMutableList()

    parsed.sortWith { c1, c2 -> (c1 to c2).compare().result }

    return (parsed.indexOf(listOf(listOf(6))) + 1) * (parsed.indexOf(listOf(listOf(2))) + 1)
}

private fun MutableList<Any>.swap(first: Int, second: Int) {
    val temp = this[first]
    this[first] = this[second]
    this[second] = temp
}

private fun Pair<Any, Any>.compare(): Decision {
    while (true) {
        val result: Decision?
        if ((this.first is Int) && (this.second is Int)) {
            result = compareInts(this.first as Int, this.second as Int)
        } else {
            val firstAsList = this.first.castToList()
            val secondAsList = this.second.castToList()
            (0 until min(firstAsList.size, secondAsList.size)).forEach { index ->
                val compare = (firstAsList[index] to secondAsList[index]).compare()
                if (compare != Decision.INDECISION) {
                    return compare
                }
            }

            result = compareListsSize(firstAsList, secondAsList)

            if (result != Decision.INDECISION) {
                return result
            }
        }

        return result
    }
}

private fun compareListsSize(
    firstAsList: List<Any>,
    secondAsList: List<Any>
) = when (firstAsList.size.compareTo(secondAsList.size)) {
    1 -> Decision.WRONG
    0 -> Decision.INDECISION
    -1 -> Decision.RIGHT
    else -> {
        throw Error("Can't be")
    }
}

private fun Any.castToList(): List<Any> {
    if (this is Int) return listOf(this)
    return this as List<Any>
}

private fun compareInts(first: Int, second: Int) = when (first - second) {
    in (1..Int.MAX_VALUE) -> Decision.WRONG
    in (Int.MIN_VALUE..-1) -> Decision.RIGHT
    else -> Decision.INDECISION
}

private enum class Decision(val result: Int) {
    RIGHT(-1), WRONG(1), INDECISION(0)
}

private data class Wrapper(
    val wrapping: List<Any>
)

private fun String.parseLine(): List<Any> {
    return Klaxon().parse<Wrapper>(this)!!.wrapping
}
