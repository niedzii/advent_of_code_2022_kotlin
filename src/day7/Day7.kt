package day7

import java.io.File
import java.util.function.BiFunction
import kotlin.math.absoluteValue

fun main() {
    val input = File("src/day7", "day7_input.txt").readLines().flatMap { it.split(",") }.map { it.toInt() }

    println("Stage 1 answer is ${stageOne(input)}") // 354129
    println("Stage 2 answer is ${stageTwo(input)}") // 98905973
}

fun stageOne(input: List<Int>): Int {
    val calculator =
        { coordinates: List<Int>, coordinate: Int -> coordinates.map { (coordinate - it).absoluteValue }.sum() }
    return calculateBestPosition(input, calculator)
}

fun stageTwo(input: List<Int>): Int {
    val calculator =
        { coordinates: List<Int>, coordinate: Int -> coordinates.map {
            var result = 0
            for (i in 1..(coordinate - it).absoluteValue) {
                result += i
            }
            result
        }.sum() }
    return calculateBestPosition(input, calculator)
}

private fun calculateBestPosition(input: List<Int>, calculator: BiFunction<List<Int>, Int, Int>): Int {
    var bestIndex = input.average().toInt()
    var currentBest = calculator.apply(input, bestIndex)
    while (true) {
        val toTheLeft = calculator.apply(input, bestIndex - 1)
        val toTheRight = calculator.apply(input, bestIndex + 1)

        if (toTheLeft > currentBest && toTheRight > currentBest) {
            break
        }

        if (toTheLeft < currentBest) {
            bestIndex -= 1
            currentBest = toTheLeft
        }

        if (toTheRight < currentBest) {
            bestIndex += 1
            currentBest = toTheRight
        }

    }

    return currentBest
}
