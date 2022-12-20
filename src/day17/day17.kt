package day17

import java.io.File
import utils.Grid

private const val AIR = 0L
private const val STONE = 1L
private const val FLOOR = 2L

fun main() {
    val input = File("src/day17", "day17_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 3193
    val stageTwo = stageTwo(input)
    val correct = 1577650429835
    println("Stage 2 answer is $stageTwo") //
    println("Stage 2 answer should be $correct") //

    println("diff is ${stageTwo - correct}")

    println("-------------test-------------")
    println(stageTwo(listOf(">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>")) == 1514285714288)

}

fun stageOne(input: List<String>): Long {
    val instructions = input.first().map { Direction.toDirection(it) }
    val height = 4_000L
    val grid = Grid17(height, 7) { AIR }.apply { this.addFloor(height) }

    return grid.playPartOne(instructions, height, 2022L)
}

fun stageTwo(input: List<String>): Long {
    val instructions = input.first().map { Direction.toDirection(it) }
    val height = 1_000_000L
    val elements = 1_000_000_000_000L
    val grid = Grid17(height, 7) { AIR }.apply { this.addFloor(height) }

    return grid.playPartTwo(instructions, height, elements)
}

private enum class Direction(val yOffset: Long) {
    LEFT(-1L),
    RIGHT(1L);

    companion object {
        fun toDirection(code: Char): Direction {
            return when (code) {
                '<' -> LEFT
                '>' -> RIGHT
                else -> error("F")
            }
        }
    }
}

private class Grid17(height: Long, width: Long, init: (index: Int) -> Long) :
    Grid<Long>(height.toInt(), width.toInt(), init) {
    fun getHorizontal(height: Long): List<Pair<Long, Long>> {
        return listOf(2L to height, 3L to height, 4L to height, 5L to height)
    }

    fun getPlus(height: Long): List<Pair<Long, Long>> {
        return listOf(3L to height - 2, 2L to height - 1, 3L to height - 1, 4L to height - 1, 3L to height)
    }

    fun getL(height: Long): List<Pair<Long, Long>> {
        return listOf(4L to (height - 2), 4L to (height - 1), 4L to height, 3L to height, 2L to height)
    }

    fun getVertical(height: Long): List<Pair<Long, Long>> {
        return listOf(2L to height - 3, 2L to height - 2, 2L to height - 1, 2L to height)
    }

    fun getSquare(height: Long): List<Pair<Long, Long>> {
        return listOf(2L to height - 1, 2L to height, 3L to height - 1, 3L to height)
    }

    fun getElement(index: Long, height: Long): List<Pair<Long, Long>> {
        return when (index % 5) {
            0L -> getHorizontal(height)
            1L -> getPlus(height)
            2L -> getL(height)
            3L -> getVertical(height)
            4L -> getSquare(height)
            else -> {
                error("F")
            }
        }
    }


    fun putPoints(poLongs: List<Pair<Long, Long>>) {
        poLongs.forEach {
            put(it.first.toInt(), it.second.toInt(), STONE)
        }
    }

    fun playPartOne(instructions: List<Direction>, height: Long, elements: Long): Long {
        var score: Long = 0

        var elementIndex = 0L
        var highestPoint = height - 1
        var instructionIndex = 0


        while (elementIndex < elements) {
            var points = getElement(elementIndex++, highestPoint - 4L)

            while (true) {
                if (canGoSideways(points, instructions[instructionIndex % instructions.size])) {
                    points = goSideways(points, instructions[instructionIndex % instructions.size])
                }
                if (canGoDown(points)) {
                    points = goDown(points)
                } else {
                    putPoints(points)
                    val newHeight = points.minOf { it.second }
                    if (newHeight < highestPoint) {

                        val addition = highestPoint - newHeight

                        score += addition

                        highestPoint = newHeight

                    }
                    instructionIndex++
                    break
                }
                instructionIndex++
            }
        }
        return score
    }

    fun playPartTwo(instructions: List<Direction>, height: Long, elements: Long): Long {
        var score: Long = 0

        var elementIndex = 0L
        var highestPoint = height - 1
        var instructionIndex = 0


        while (elementIndex < elements) {
            var points = getElement(elementIndex++, highestPoint - 4L)

            while (true) {
                if (canGoSideways(points, instructions[instructionIndex % instructions.size])) {
                    points = goSideways(points, instructions[instructionIndex % instructions.size])
                }
                if (canGoDown(points)) {
                    points = goDown(points)
                } else {
                    putPoints(points)
                    val newHeight = points.minOf { it.second }
                    if (newHeight < highestPoint) {

                        val addition = highestPoint - newHeight

                        score += addition

                        highestPoint = newHeight

                    }
                    instructionIndex++
                    break
                }
                instructionIndex++
            }
        }
        return score
    }

    fun addFloor(height: Long) {
        (0..6).forEach {
            this.put(it, height.toInt() - 1, FLOOR)
        }
    }

    private fun canGoDown(poLongs: List<Pair<Long, Long>>): Boolean {
        return poLongs.all { get(it.first.toInt(), it.second.toInt() + 1) == AIR }
    }

    private fun goDown(poLongs: List<Pair<Long, Long>>): List<Pair<Long, Long>> {
        return poLongs.map { it.copy(first = it.first, second = it.second + 1) }
    }

    private fun canGoSideways(poLongs: List<Pair<Long, Long>>, direction: Direction): Boolean {
        return poLongs.all { getOrNull(it.first.toInt() + direction.yOffset.toInt(), it.second.toInt()) == AIR }
    }

    private fun goSideways(poLongs: List<Pair<Long, Long>>, direction: Direction): List<Pair<Long, Long>> {
        return poLongs.map { it.copy(first = it.first + direction.yOffset, second = it.second) }
    }
}
