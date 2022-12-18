package day18

import java.io.File

fun main() {
    val input = File("src/day18", "day18_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 4314
    println("Stage 2 answer is ${stageTwo(input)}") // 2444
}

fun stageOne(input: List<String>): Int {
    val cubes = input.map { it.parse() }
    var score = cubes.size * 6

    cubes.forEach {
        it.getAdjacentCoordinates().forEach {
            if (cubes.contains(it)) {
                score -= 1
            }
        }
    }

    return score
}

fun stageTwo(input: List<String>): Int {
    val cubes = input.map { it.parse() }
    var score = 0

    val xBound = cubes.minOf { it.first } - 1..cubes.maxOf { it.first } + 1
    val yBound = cubes.minOf { it.second } - 1..cubes.maxOf { it.second } + 1
    val zBound = cubes.minOf { it.third } - 1..cubes.maxOf { it.third } + 1

    val toVisit = mutableListOf(Triple(xBound.first, yBound.first, zBound.first))
    val visited = mutableSetOf<Triple<Int, Int, Int>>()


    while (toVisit.isNotEmpty()) {
        val element = toVisit.removeFirst()

        if (element !in visited) {
            element.getAdjacentInRange(xBound, yBound, zBound)
                .forEach { adjacent ->
                    if (adjacent in cubes) {
                        score++
                    } else {
                        toVisit.add(adjacent)
                        visited.add(element)
                    }
                }
        }
    }

    return score
}

private fun String.parse(): Triple<Int, Int, Int> {
    val split = this.split(",").map { it.toInt() }
    return Triple(split[0], split[1], split[2])
}

private fun Triple<Int, Int, Int>.getAdjacentCoordinates(): List<Triple<Int, Int, Int>> {
    val adjacent = listOf(
        Triple(-1, 0, 0),
        Triple(1, 0, 0),
        Triple(0, -1, 0),
        Triple(0, 1, 0),
        Triple(0, 0, -1),
        Triple(0, 0, 1)
    )

    return adjacent.map {
        this.copy(
            first = first + it.first,
            second = second + it.second,
            third = third + it.third
        )
    }
}

private fun Triple<Int, Int, Int>.getAdjacentInRange(
    xBound: IntRange,
    yBound: IntRange,
    zBound: IntRange
) = this.getAdjacentCoordinates()
    .filter { it.first in xBound && it.second in yBound && it.third in zBound }
