package day1

import java.io.File

fun main() {
    val scannerReadings = File("src/day1", "day1_input.txt").readLines().map { it.toInt() }
    println("Stage 1 answer is ${stageOne(scannerReadings)}") // 1692
    println("Stage 2 answer is ${stageTwo(scannerReadings)}") // 1724
}

fun stageTwo(scannerReadings: List<Int>): Int {
    return scannerReadings.windowed(3, 1).zipWithNext().count { it.first.sum() < it.second.sum() }
}

fun stageOne(scannerReadings: List<Int>): Int {
    return scannerReadings.zipWithNext().count { it.first < it.second }
}