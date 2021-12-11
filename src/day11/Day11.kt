package day11

import java.io.File
import java.lang.IndexOutOfBoundsException

fun main() {
    val input = File("src/day11", "day11_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 1688
    println("Stage 2 answer is ${stageTwo(input)}") // 403
}

var numberOfFlashesPartOne = 0
val ADJACENT = listOf(
    Pair(1, 0),
    Pair(-1, 0),
    Pair(0, 1),
    Pair(0, -1),
    Pair(1, 1),
    Pair(1, -1),
    Pair(-1, 1),
    Pair(-1, -1)
)

fun stageOne(input: List<String>): Int {
    val numberOfSteps = 100
    val grid = input.map { it.toCharArray().map { char -> char.digitToInt() }.toMutableList() }.toMutableList()
    for (step in 0 until numberOfSteps) {
        incrementEnergy(grid)

        grid.forEachIndexed { indexX, line ->
            line.forEachIndexed { indexY, _ ->
                if (grid[indexX][indexY] > 9) {
                    flash(indexX, indexY, grid)
                }
            }
        }

        grid.forEachIndexed { indexX, line ->
            line.forEachIndexed { indexY, _ ->
                if (grid[indexX][indexY] == -1) {
                    grid[indexX][indexY] = 0
                }
            }
        }
    }

    return numberOfFlashesPartOne
}

fun flash(x: Int, y: Int, grid: MutableList<MutableList<Int>>) {
    grid[x][y] = -1
    numberOfFlashesPartOne++

    for (pair in ADJACENT) {
        try {

            if (grid[x + pair.first][y + pair.second] != -1) {

                grid[x + pair.first][y + pair.second] += 1
                if (grid[x + pair.first][y + pair.second] >= 10) {
                    flash(x + pair.first, y + pair.second, grid)
                }
            }
        } catch (e: IndexOutOfBoundsException) {
        }
    }
}



fun incrementEnergy(grid: MutableList<MutableList<Int>>) {
    for (i in 0 until grid.size) {
        for (j in 0 until grid[0].size) {
            grid[i][j] += 1
        }
    }
}

fun stageTwo(input: List<String>): Int {
    val grid = input.map { it.toCharArray().map { char -> char.digitToInt() }.toMutableList() }.toMutableList()
    var numberOfSteps = 0
    while(true) {
        numberOfSteps++
        incrementEnergy(grid)

        grid.forEachIndexed { indexX, line ->
            line.forEachIndexed { indexY, _ ->
                if (grid[indexX][indexY] > 9) {
                    flash(indexX, indexY, grid)
                }
            }
        }

        var allFlashed = true
        grid.forEachIndexed { indexX, line ->
            line.forEachIndexed { indexY, _ ->
                if (grid[indexX][indexY] == -1) {
                    grid[indexX][indexY] = 0
                } else {
                    allFlashed = false
                }
            }
        }

        if(allFlashed) return numberOfSteps
    }
}