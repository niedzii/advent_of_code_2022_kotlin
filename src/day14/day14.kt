package day14

import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min

private const val AIR = 0
private const val STONE = 1
private const val SAND = 2
private const val SAND_INDEX = 500
private val DOWN = 1 to 0
private val LEFT_DOWN = 1 to -1
private val RIGHT_DOWN = 1 to 1

private var LOWEST_Y = 0

fun main() {
    val input = File("src/day14", "day14_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 961
    println("Stage 2 answer is ${stageTwo(input)}") // 26375
}

fun stageOne(input: List<String>): Int {
    return calculateSandCapacity(input, false)
}

fun stageTwo(input: List<String>): Int {
    return calculateSandCapacity(input, true)
}

private fun calculateSandCapacity(input: List<String>, withFloor: Boolean): Int {
    val grid = createGrid()
    putStones(input, grid)

    if(withFloor) putFloor(LOWEST_Y, grid)

    var score = 0
    while(true) {
        try{
            dropSand(grid)
            score++
        } catch (e: IndexOutOfBoundsException) {
            return score
        }
    }
}

private fun putFloor(lowestY: Int, grid: MutableList<MutableList<Int>>) {
    val level = lowestY + 2
    ((0 to level) to (999 to level)).drawLine(grid)
}

private fun dropSand(grid: MutableList<MutableList<Int>>) {
    var current = 0 to SAND_INDEX

    while (true) {
        if(grid[0][SAND_INDEX] == SAND) throw IndexOutOfBoundsException("XD")

        val down = current.add(DOWN)
        val leftDown = current.add(LEFT_DOWN)
        val rightDown = current.add(RIGHT_DOWN)
        if (grid[down.first][down.second] == AIR) {
            current = down
        } else if (grid[leftDown.first][leftDown.second] == AIR) {
            current = leftDown
        } else if (grid[rightDown.first][rightDown.second] == AIR) {
            current = rightDown
        } else {
            grid[current.first][current.second] = SAND
            break
        }
    }
}

private fun Pair<Int, Int>.add(other: Pair<Int, Int>) =
    this.copy(first = first + other.first, second = second + other.second)

private fun Pair<Pair<Int, Int>, Pair<Int, Int>>.drawLine(grid: MutableList<MutableList<Int>>) {
    if (this.first.first == this.second.first) {
        val min = min(this.first.second, this.second.second)
        val max = max(this.first.second, this.second.second)
        if(max > LOWEST_Y) LOWEST_Y = max

        (min..max).forEach { x ->
            grid[x][this.first.first] = STONE
        }

    } else {
        val min = min(this.first.first, this.second.first)
        val max = max(this.first.first, this.second.first)

        (min..max).forEach { y ->
            grid[this.first.second][y] = STONE
        }

    }
}

private fun putStones(
    input: List<String>,
    grid: MutableList<MutableList<Int>>
) {
    input.map { it.split(" -> ") }.map {
        it.map { it.split(",").let { it[0].toInt() to it[1].toInt() } }
    }.forEach { it.windowed(2).map { it[0] to it[1] }.forEach { it.drawLine(grid) } }
}

private fun createGrid(): MutableList<MutableList<Int>> = MutableList(1000) { MutableList(1000) { AIR }
}

