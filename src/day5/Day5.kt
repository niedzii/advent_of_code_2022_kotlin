package day5

import java.io.File
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sign

fun main() {
    val lines = File("src/day5", "day5_input.txt").readLines()

    println("Stage 1 answer is ${stageOne(lines)}") // 7644
    println("Stage 2 answer is ${stageTwo(lines)}") // 18627
}

fun stageOne(input: List<String>): Int {
    return countIntersects(input, false)
}

fun stageTwo(input: List<String>): Int {
    return countIntersects(input, true)
}

fun countIntersects(lines : List<String>, diagonals: Boolean): Int {
    val grid = mutableMapOf<Pair<Int, Int>, Int>()

    lines.forEach { line ->
        val coordinates = line.split(" -> ")
        val firstCoordinate = coordinates[0].split(",").map { it.toInt() }
        val secondCoordinate = coordinates[1].split(",").map { it.toInt() }

        val xIncline = (firstCoordinate[0] - secondCoordinate[0]).sign
        val yIncline = (firstCoordinate[1] - secondCoordinate[1]).sign

        val h = max(
            (firstCoordinate[0] - secondCoordinate[0]).absoluteValue,
            (firstCoordinate[1] - secondCoordinate[1]).absoluteValue
        )

        for (i in 0..h) {
            if (!diagonals && xIncline != 0 && yIncline != 0) break
            val currentCoordinate = Pair(firstCoordinate[0] + i * -xIncline, firstCoordinate[1] + i * -yIncline)
            grid[currentCoordinate] =
                (grid[currentCoordinate] ?: 0).plus(1)
        }
    }

    return grid.count { it.value > 1 }
}