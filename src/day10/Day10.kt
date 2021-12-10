package day10

import java.io.File
import java.util.*

val SIGNS = mapOf(Pair(')', '('), Pair('>', '<'), Pair('}', '{'), Pair(']', '['))

fun main() {
    val input = File("src/day10", "day10_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 344193
    println("Stage 2 answer is ${stageTwo(input)}") // 3241238967
}

fun stageOne(input: List<String>): Int {
    val stack = Stack<Char>()
    var result = 0
    input.forEach { line ->
        for (it in line) {
            if (!isClosing(it)) {
                stack.push(it)
            } else {
                if (SIGNS[it] == stack.peek()) {
                    stack.pop()
                } else {
                    result += calculatePointsStageOne(it)
                    break
                }
            }
        }
    }
    return result
}

private fun calculatePointsStageOne(char: Char): Int {
    return when (char) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> 0
    }
}

private fun isClosing(char: Char) = SIGNS.keys.contains(char)

fun stageTwo(input: List<String>): Long {
    val results = mutableListOf<Long>()
    input.forEach { line ->
        val stack = Stack<Char>()
        var isCorrupted = false
        for (it in line) {
            if (!isClosing(it)) {
                stack.push(it)
            } else {
                if (SIGNS[it] == stack.peek()) {
                    stack.pop()
                } else {
                    isCorrupted = true
                    break
                }
            }
        }
        if (!isCorrupted && !stack.empty()) {
            //incomplete
            var result : Long = 0
            stack.toList().asReversed().map(::mapToPointsStageTwo).forEach {
                result *= 5L
                result += it
            }
            results.add(result)

        }
    }
    return results.sorted()[results.size / 2]
}

fun mapToPointsStageTwo(char : Char) : Int {
    return when (char) {
        '(' -> 1
        '[' -> 2
        '{' -> 3
        '<' -> 4
        else -> 0
    }
}
