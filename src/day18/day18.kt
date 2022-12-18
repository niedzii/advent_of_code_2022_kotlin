package day18

import java.io.File

fun main() {
    val input = File("src/day18", "day18_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 
    println("Stage 2 answer is ${stageTwo(input)}") // 
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
    var score = cubes.size * 6

    cubes.forEach { cube ->
        cube.getAdjacentCoordinates().forEach { adjacent ->
            if (adjacent.getAdjacentCoordinates().all { cubes.contains(it) }) {
                // interior pocket of air
                println(adjacent)
                score -= 1
            } else if (cubes.contains(adjacent)) {
                score -= 1
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
