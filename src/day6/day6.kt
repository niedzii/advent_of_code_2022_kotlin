package day6

import java.io.File

fun main() {
    val input = File("src/day6", "day6_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 1855
    println("Stage 2 answer is ${stageTwo(input)}") // 3256
}

fun stageOne(input: List<String>): Int {
    return input.first().getIndexOfUniqueWindows(4)
}

fun stageTwo(input: List<String>): Int {
    return input.first().getIndexOfUniqueWindows(14)
}

private fun String.getIndexOfUniqueWindows(windowSize: Int): Int {
    this.toCharArray().toList().windowed(windowSize, 1)
        .forEachIndexed { index, chars -> (if (chars.distinct().size == windowSize) return index + windowSize)}
    return -1
}