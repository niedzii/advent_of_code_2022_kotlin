package day3

import java.io.File

fun main() {
    val input = File("src/day3", "day3_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 7691
    println("Stage 2 answer is ${stageTwo(input)}") // 2508
}

fun stageOne(input: List<String>): Int {
    return input.map { it.substring(0, it.length / 2) to it.substring(it.length / 2, it.length) }
        .map { it.first.toCharArray().toList() to it.second.toCharArray().toList() }
        .flatMap { it.first.intersect(it.second) }
        .sumOf { it.assignPoints() }
}

fun stageTwo(input: List<String>): Int {
    return input
        .windowed(3, 3)
        .map { it.getBadge() }
        .sumOf { it.assignPoints() }
}

private fun List<String>.getBadge() =
    this[0].toCharArray().intersect(this[1].toCharArray().toList().toSet()).intersect(
        this[2].toCharArray().toList()
            .toSet()
    )
        .toList()[0]

// using ascii codes
private fun Char.assignPoints(): Int {
    return if (this.isUpperCase()) {
        this.code - 38
    } else {
        this.code - 96
    }
}
