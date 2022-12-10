package day9

import java.io.File
import kotlin.RuntimeException
import kotlin.math.absoluteValue

fun main() {
    val input = File("src/day9", "day9_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 6314
    println("Stage 2 answer is ${stageTwo(input)}") // 2504
}

fun stageOne(input: List<String>): Int {
    val instructions = input.map { it.parseInstruction() }
    val visitedPoints = mutableSetOf<Pair<Int, Int>>()

    var headPosition = 0 to 0
    var tailPosition = 0 to 0

    instructions.forEach {
        val (newHead, newTail) = it.useInstructionFirst(headPosition, tailPosition, visitedPoints)
        headPosition = newHead
        tailPosition = newTail
    }

    return visitedPoints.size
}

fun stageTwo(input: List<String>): Int {
    val instructions = input.map { it.parseInstruction() }
    val visitedPoints = mutableSetOf<Pair<Int, Int>>()
    val points = MutableList(10) { 0 to 0 }

    instructions.forEach { pair ->
        pair.useInstructionSecond(points, visitedPoints)
    }

    return visitedPoints.size
}

private fun Pair<Direction, Int>.useInstructionSecond(
    points: MutableList<Pair<Int, Int>>,
    visitedPoints: MutableSet<Pair<Int, Int>>
): MutableList<Pair<Int, Int>> {
    (0 until this.second).forEach { _ ->
        (0..8).forEach { index ->
            val head = points[index]
            val tail = points[index + 1]

            if (index == 0) {
                //move head
                val (newHead, newTail) = moveHeadAndTail(head, tail, this.first)
                points[0] = newHead
                points[1] = newTail
            } else {
                val newTail = moveOnlyTail(head, tail)
                points[index + 1] = newTail

                if (index == 8) {
                    visitedPoints.add(newTail)
                }
            }
        }
    }
    return points
}

private fun Pair<Direction, Int>.useInstructionFirst(
    headPosition: Pair<Int, Int>,
    tailPosition: Pair<Int, Int>,
    visitedPoints: MutableSet<Pair<Int, Int>>
): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    var currentHead = headPosition
    var currentTail = tailPosition
    (0 until this.second).forEach { _ ->
        val (newHead, newTail) = moveHeadAndTail(currentHead, currentTail, this.first)
        currentHead = newHead
        currentTail = newTail
        visitedPoints.add(currentTail)
    }
    return currentHead to currentTail
}

private fun moveHeadAndTail(
    headPosition: Pair<Int, Int>,
    tailPosition: Pair<Int, Int>,
    direction: Direction
): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val newHeadPosition = headPosition.move(direction)
    val newTailPosition = moveTail(newHeadPosition, tailPosition)
    return newHeadPosition to newTailPosition
}

private fun moveOnlyTail(headPosition: Pair<Int, Int>, tailPosition: Pair<Int, Int>): Pair<Int, Int> {
    return moveTail(headPosition, tailPosition)
}

private fun moveTail(headPosition: Pair<Int, Int>, tailPosition: Pair<Int, Int>): Pair<Int, Int> {
    val xDiff = (headPosition.first - tailPosition.first)
    val yDiff = (headPosition.second - tailPosition.second)

    if (isAdjacent(xDiff, yDiff)) {
        return tailPosition
    } else {
        if (xDiff == 0) {
            if (yDiff == -2) return tailPosition.move(Direction.DOWN)
            if (yDiff == 2) return tailPosition.move(Direction.UP)
        } else if (yDiff == 0) {
            if (xDiff == -2) return tailPosition.move(Direction.LEFT)
            if (xDiff == 2) return tailPosition.move(Direction.RIGHT)
        }

        if(xDiff.absoluteValue == 2 && yDiff.absoluteValue == 2) {
            var temp = tailPosition

            temp = if(xDiff > 0) {
                temp.move(Direction.RIGHT)
            } else {
                temp.move(Direction.LEFT)
            }

            temp = if(yDiff > 0) {
                temp.move(Direction.UP)
            } else {
                temp.move(Direction.DOWN)
            }
            return temp
        }

        var temp = tailPosition
        temp = if (xDiff > 0) {
            tailPosition.move(Direction.RIGHT)
        } else {
            temp.move(Direction.LEFT)
        }

        temp = if (yDiff > 0) {
            temp.move(Direction.UP)
        } else {
            temp.move(Direction.DOWN)
        }
        return temp
    }
}

internal fun isAdjacent(xDiff: Int, yDiff: Int) = xDiff in -1..1 && yDiff in -1..1

private fun Pair<Int, Int>.move(direction: Direction): Pair<Int, Int> =
    when (direction) {
        Direction.RIGHT -> {
            this.copy(first = this.first + 1)
        }
        Direction.LEFT -> {
            this.copy(first = this.first - 1)
        }
        Direction.UP -> {
            this.copy(second = this.second + 1)
        }
        Direction.DOWN -> {
            this.copy(second = this.second - 1)
        }
    }

private fun String.parseInstruction(): Pair<Direction, Int> {
    return Direction.toDirection(this[0]) to this.substring(2).toInt()
}


enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    companion object {
        fun toDirection(code: Char) =
            when (code) {
                'U' -> UP
                'D' -> DOWN
                'L' -> LEFT
                'R' -> RIGHT
                else -> throw RuntimeException()
            }
    }
}
