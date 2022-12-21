package day17

import java.io.File
import java.util.HashSet
import java.util.Random
import java.util.UUID
import utils.Grid

typealias Top = Long
typealias Blocks = Long

fun main() {
    val input = File("src/day17", "day17_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 3193
    println("Stage 2 answer is ${stageTwo(input)}") // 1577650429835
}

fun stageOne(input: List<String>): Long {
    val instructions = input.first().map { Direction.toDirection(it) }
    val howMuchToDrop = 2022L
    return simulate(howMuchToDrop, instructions)
}

fun stageTwo(input: List<String>): Long {
    val instructions = input.first().map { Direction.toDirection(it) }
    val howMuchToDrop = 1_000_000_000_000L
    return simulate(howMuchToDrop, instructions)
}

private fun simulate(howMuchToDrop: Long, instructions: List<Direction>): Long {
    var top: Long = 0
    var skipped: Long = 0
    val elements = mutableSetOf<Pair<Long, Long>>()
    var directionIndex = 0L
    var index: Long = 0

    val cache = HashMap<Snapshot, Pair<Blocks, Top>>()

    (0 until 7L).forEach {
        elements.add(it to 0L)
    }

    while (index < howMuchToDrop) {
        var element = getElement(index, top + 4L)
        while (true) {
            val direction = instructions.getNext(directionIndex++)

            if (direction == Direction.LEFT) {
                element = element.moveLeft()
                if (element.any { elements.contains(it) }) {
                    element = element.moveRight()
                }
            } else {
                element = element.moveRight()
                if (element.any { elements.contains(it) }) {
                    element = element.moveLeft()
                }
            }

            element = element.moveDown()

            // it settles
            if (element.any { elements.contains(it) }) {
                element = element.moveUp()
                elements.addAll(element)
                top = elements.maxOf { it.second }


                // check if you've been in this state before
                val snapshot = Snapshot(index % 5, directionIndex % instructions.size, elements.lastRowsSnapshot(top))
                val cached = cache[snapshot]

                // if yes, calculate how much you can skip
                if (cached != null) {
                    val deltaHeight = top - cached.second
                    val deltaBlocks = index - cached.first

                    val cycles = (howMuchToDrop - index) / deltaBlocks

                    skipped += (deltaHeight * cycles)
                    index += (deltaBlocks * cycles)

                }
                cache[snapshot] = index to top
                break
            }
        }
        index++

    }

    return top + skipped
}

private fun Set<Pair<Long, Long>>.lastRowsSnapshot(maxHeight: Long): Set<Pair<Long, Long>> {
    val howMuch = 30L

    return this.filter { it.second in (maxHeight - howMuch..maxHeight) }
        .map { it.copy(first = it.first, second = maxHeight - it.second) }.toSet()
}

private fun List<Direction>.getNext(index: Long) = this[index.toInt() % this.size]

private fun Set<Pair<Long, Long>>.moveRight(): Set<Pair<Long, Long>> {
    return if (this.any { it.first == 6L }) {
        this
    } else {
        this.map { it.copy(first = it.first + 1, second = it.second) }.toSet()
    }
}

private fun Set<Pair<Long, Long>>.moveLeft(): Set<Pair<Long, Long>> {
    return if (this.any { it.first == 0L }) {
        this
    } else {
        this.map { it.copy(first = it.first - 1, second = it.second) }.toSet()
    }
}

private fun Set<Pair<Long, Long>>.moveDown(): Set<Pair<Long, Long>> {
    return this.map { it.copy(first = it.first, second = it.second - 1) }.toSet()
}

private fun Set<Pair<Long, Long>>.moveUp(): Set<Pair<Long, Long>> {
    return this.map { it.copy(first = it.first, second = it.second + 1) }.toSet()
}

private fun getHorizontal(height: Long): Set<Pair<Long, Long>> {
    return setOf(2L to height, 3L to height, 4L to height, 5L to height)
}

private fun getPlus(height: Long): Set<Pair<Long, Long>> {
    return setOf(3L to height + 2, 2L to height + 1, 3L to height + 1, 4L to height + 1, 3L to height)
}

private fun getL(height: Long): Set<Pair<Long, Long>> {
    return setOf(4L to (height + 2), 4L to (height + 1), 4L to height, 3L to height, 2L to height)
}

private fun getVertical(height: Long): Set<Pair<Long, Long>> {
    return setOf(2L to height + 3, 2L to height + 2, 2L to height + 1, 2L to height)
}

private fun getSquare(height: Long): Set<Pair<Long, Long>> {
    return setOf(2L to height + 1, 2L to height, 3L to height + 1, 3L to height)
}

private fun getElement(index: Long, height: Long): Set<Pair<Long, Long>> {
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

private data class Snapshot(
    val elementIndex: Long,
    val directionIndex: Long,
    val topRowSnapshot: Set<Pair<Long, Long>>
)

private enum class Direction {
    LEFT,
    RIGHT;

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
