package day23

import java.io.File

const val ELF = '#'
const val EMPTY = '.'
var SEQUENCE = 0
var DECISION_ROUND_INDEX = 0

fun main() {
    val input = File("src/day23", "day23_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 4116
    println("Stage 2 answer is ${stageTwo(input)}") // 984
}

fun stageOne(input: List<String>): Int {
    val elves = input.extractElfPositions()
    val rounds = 10

    repeat(rounds) {
        val proposedMoves = mutableMapOf<Pair<Int, Int>, MutableList<Elf>>()
        // part 1
        elves.forEach {
            if (it.hasAnyNeighbour(elves)) {
                val proposition = it.proposeMove(elves)?.getCords(it)
                if (proposition != null) {
                    proposedMoves.putIfAbsent(proposition, mutableListOf())
                    proposedMoves[proposition]!!.add(it)
                }
            }
        }

        DECISION_ROUND_INDEX++
        // part 2
        proposedMoves.filter { it.value.size == 1 }.forEach {
            it.value.first().move(it.key)
        }

    }

    return calculateScore(elves)
}

fun stageTwo(input: List<String>): Int {
    val elves = input.extractElfPositions()
    var round = 0
    while(true) {
        round++
        val proposedMoves = mutableMapOf<Pair<Int, Int>, MutableList<Elf>>()
        // part 1
        elves.forEach {
            if (it.hasAnyNeighbour(elves)) {
                val proposition = it.proposeMove(elves)?.getCords(it)
                if (proposition != null) {
                    proposedMoves.putIfAbsent(proposition, mutableListOf())
                    proposedMoves[proposition]!!.add(it)
                }
            }
        }

        DECISION_ROUND_INDEX++
        // part 2
        if(proposedMoves.filter { it.value.size == 1 }.isEmpty()) {
            return round
        }

        proposedMoves.filter { it.value.size == 1 }.forEach {
            it.value.first().move(it.key)
        }

    }
}

private fun calculateScore(elves: List<Elf>): Int {
    val minX = elves.minOf { it.x }
    val maxX = elves.maxOf { it.x }
    val minY = elves.minOf { it.y }
    val maxY = elves.maxOf { it.y }

    return (maxX - minX + 1) * (maxY - minY + 1) - elves.count()
}

private fun List<String>.extractElfPositions(): List<Elf> {
    val result = mutableListOf<Elf>()

    this.forEachIndexed { y, row ->
        row.forEachIndexed { x, tile ->
            if (tile == ELF) result.add(Elf(x, y))

        }
    }

    return result
}

private data class Elf(
    var x: Int,
    var y: Int,
    var index: Int = SEQUENCE++
) {

    fun proposeMove(elves: List<Elf>): Direction? {
        if ((DECISION_ROUND_INDEX) % 4 == 0) {
            if (canGoNorth(elves)) {
                return Direction.N
            }
            if (canGoSouth(elves)) {
                return Direction.S
            }
            if (canGoWest(elves)) {
                return Direction.W
            }
            if (canGoEast(elves)) {
                return Direction.E
            }
        }

        if ((DECISION_ROUND_INDEX) % 4 == 1) {
            if (canGoSouth(elves)) {
                return Direction.S
            }
            if (canGoWest(elves)) {
                return Direction.W
            }
            if (canGoEast(elves)) {
                return Direction.E
            }
            if (canGoNorth(elves)) {
                return Direction.N
            }
        }

        if ((DECISION_ROUND_INDEX) % 4 == 2) {
            if (canGoWest(elves)) {
                return Direction.W
            }
            if (canGoEast(elves)) {
                return Direction.E
            }
            if (canGoNorth(elves)) {
                return Direction.N
            }
            if (canGoSouth(elves)) {
                return Direction.S
            }
        }

        if ((DECISION_ROUND_INDEX) % 4 == 3) {
            if (canGoEast(elves)) {
                return Direction.E
            }
            if (canGoNorth(elves)) {
                return Direction.N
            }
            if (canGoSouth(elves)) {
                return Direction.S
            }
            if (canGoWest(elves)) {
                return Direction.W
            }
        }

        return null
    }

    private fun canGoEast(elves: List<Elf>) = all(
        listOf(
            isEmpty(this, Direction.E, elves),
            isEmpty(this, Direction.NE, elves),
            isEmpty(this, Direction.SE, elves)
        )
    )

    private fun canGoWest(elves: List<Elf>) = all(
        listOf(
            isEmpty(this, Direction.W, elves),
            isEmpty(this, Direction.NW, elves),
            isEmpty(this, Direction.SW, elves)
        )
    )

    private fun canGoSouth(elves: List<Elf>) = all(
        listOf(
            isEmpty(this, Direction.S, elves),
            isEmpty(this, Direction.SE, elves),
            isEmpty(this, Direction.SW, elves)
        )
    )

    private fun canGoNorth(elves: List<Elf>) = all(
        listOf(
            isEmpty(this, Direction.N, elves),
            isEmpty(this, Direction.NE, elves),
            isEmpty(this, Direction.NW, elves)
        )
    )

    fun move(to: Pair<Int, Int>) {
        this.x = to.first
        this.y = to.second
    }

    fun hasAnyNeighbour(elves: List<Elf>): Boolean {
        return listOf(
            -1 to 1,
            -1 to 0,
            -1 to -1,
            1 to 1,
            1 to 0,
            1 to -1,
            0 to -1,
            0 to 1,
        ).map { this.x + it.first to this.y + it.second }.any { elves.map { it.x to it.y }.contains(it) }
    }
}

private fun print(elves: List<Elf>) {
    val coords = elves.map { it.x to it.y }

    (0..5).forEach { row ->
        (0..4).forEach { column ->

            if (coords.contains(column to row)) {
                print(ELF)
            } else print(EMPTY)
        }
        print("\n")
    }


}

private enum class Direction(val x: Int, val y: Int) {
    N(0, -1),
    NE(1, -1),
    NW(-1, -1),
    S(0, 1),
    SE(1, 1),
    SW(-1, 1),
    E(1, 0),
    W(-1, 0);

    fun getCords(elf: Elf): Pair<Int, Int> {
        return elf.x + this.x to elf.y + this.y
    }
}


private fun isEmpty(elf: Elf, directions: Direction, elves: List<Elf>): Boolean {
    val (x, y) = directions.getCords(elf)

    return !elves.any { it.x == x && it.y == y }
}

private fun all(condition: List<Boolean>) = condition.all { it }