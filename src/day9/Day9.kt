package day9

import java.io.File
import java.util.LinkedList
import java.util.Queue

fun main() {
    val input = File("src/day9", "day9_input.txt").readLines().map { it.toCharArray().map(Character::getNumericValue) }
    println("Stage 1 answer is ${stageOne(input)}") // 572
    println("Stage 2 answer is ${stageTwo(input)}") // 847044
}

fun stageOne(input: List<List<Int>>): Int {
    return findLowestPointsCords(input).map { input[it.first][it.second] }.sumOf { it + 1 }
}

fun stageTwo(input: List<List<Int>>): Int {
    return findLowestPointsCords(input).map {
        calculateBasicSizeFromLowestPoint(input, it.first, it.second)
    }.sortedDescending().subList(0, 3).reduceRight { left, right -> left * right }
}

fun calculateBasicSizeFromLowestPoint(grid: List<List<Int>>, row: Int, col: Int): Int {

    val vis = MutableList(grid.size) { Array(grid[0].size) { false } }
    val rowCords = intArrayOf(-1, 0, 1, 0)
    val columnCords = intArrayOf(0, 1, 0, -1)

    val queue: Queue<Pair<Int, Int>> = LinkedList()
    var size = 0

    queue.add(Pair(row, col))
    vis[row][col] = true

    while (!queue.isEmpty()) {
        val cell: Pair<Int, Int> = queue.peek()
        val x: Int = cell.first
        val y: Int = cell.second
        size++
        queue.remove()

        // Go to the adjacent cells
        for (i in 0..3) {
            val adjacentX: Int = x + rowCords[i]
            val adjacentY: Int = y + columnCords[i]

            val value = try {
                grid[adjacentX][adjacentY]
            } catch (e: IndexOutOfBoundsException) {
                // if its out of grid it can be treated as peek height
                9
            }

            if (isValid(vis, adjacentX, adjacentY, value)) {
                queue.add(Pair(adjacentX, adjacentY))
                vis[adjacentX][adjacentY] = true
            }
        }
    }
    return size
}

fun isValid(vis: List<Array<Boolean>>, row: Int, col: Int, value: Int): Boolean {
    // If cell lies out of bounds or is peek height
    if (row < 0 || col < 0 || row >= vis.size || col >= vis[0].size || value == 9) return false
    return !vis[row][col]
}

fun findLowestPointsCords(grid: List<List<Int>>): List<Pair<Int, Int>> {
    val height = grid.size
    val width = grid[0].size
    val result = mutableListOf<Pair<Int, Int>>()
    grid.forEachIndexed { index, it ->
        for (i in 0 until width) {
            var previous = 10
            var next = 10
            val current = it[i]
            if (i - 1 >= 0) {
                previous = it[i - 1]
            }
            if (i + 1 < width) {
                next = it[i + 1]
            }
            if (current < next && current < previous) {
                var upper = 9
                var lower = 9

                if (index != 0) {
                    upper = grid[index - 1][i]
                }

                if (index != height - 1) {
                    lower = grid[index + 1][i]
                }

                if (current < upper && current < lower) {
                    result.add(Pair(index, i))
                }
            }
        }
    }
    return result
}
