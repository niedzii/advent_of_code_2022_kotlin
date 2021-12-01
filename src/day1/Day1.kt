package day1

import java.io.File

fun main() {
    val scannerReadings = File("src/day1", "day1_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(scannerReadings)}")
    println("Stage 2 answer is ${stageTwo(scannerReadings)}")
}

fun stageTwo(scannerReadings: List<String>): Int {
    var result = 0
    for (index in 0..scannerReadings.size - 4) {
        val first = scannerReadings.subList(index, index + 3).sumOf { it.toInt() }
        val second = scannerReadings.subList(
            index + 1,
            index + 4
        ).sumOf { it.toInt() }
        if (first < second) {
            result++
        }
    }
    return result
}

fun stageOne(scannerReadings: List<String>): Int {
    return scannerReadings.map { it.toInt() }.zipWithNext().count { it.first < it.second }
}