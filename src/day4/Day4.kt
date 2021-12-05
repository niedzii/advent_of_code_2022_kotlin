package day4

import java.io.File
import java.lang.Character.getNumericValue
import java.lang.Integer.valueOf
import kotlin.math.pow

fun main() {
    val lines = File("src/day4", "day4_input.txt").readText().split("\r\n")

    val rolledNumbers = extractRolledNumbers(lines)
    val boards = mapToBoards(lines)

    println("Stage 1 answer is ${stageOne(rolledNumbers, boards)}") // 45031
    println("Stage 2 answer is ${stageTwo(rolledNumbers, boards)}") // 2568
}

fun stageOne(rolledNumbers : List<Int>, boards : List<Board>): Int {
    val checkedNumbers = mutableListOf<Int>()

    for (rolledNumber in rolledNumbers) {
        checkedNumbers.add(rolledNumber)
        val filter = boards.filter { it.isComplete(checkedNumbers) }
        if (filter.size == 1) {
            return filter[0].calculateScore(checkedNumbers) * rolledNumber
        }
    }
    return 0
}

fun stageTwo(rolledNumbers : List<Int>, boards : List<Board>): Int {
    val checkedNumbers = mutableListOf<Int>()
    for ((index, rolledNumber) in rolledNumbers.withIndex()) {
        checkedNumbers.add(rolledNumber)
        var secondaryIndex = index
        val filter = boards.filter { !it.isComplete(checkedNumbers) }
        if (filter.size == 1) {
            while(!filter[0].isComplete(checkedNumbers)) {
                secondaryIndex++
                checkedNumbers.add(rolledNumbers[secondaryIndex + 1])
            }
            return filter[0].calculateScore(checkedNumbers) * rolledNumbers[secondaryIndex + 1]
        }
    }
    return 0
}

private fun mapToBoards(input: List<String>) =
    input.subList(2, input.size).filter { it != "" }.windowed(5, 5)
        .map { it -> it.map { it.trim().split("\\s+".toRegex()).map { valueOf(it) } } }.map { Board(it) }

private fun extractRolledNumbers(input: List<String>) =
    input[0].split(",").map { valueOf(it) }

class Board(val board : List<List<Int>>) {
    private val rotated = rotatedBoard()

    fun isComplete(rolledNumbers : List<Int>) : Boolean {
        board.plus(rotated).forEach {
            if(rolledNumbers.containsAll(it)) {
                return true
            }
        }
        return false
    }

    fun calculateScore(calledNumbers : List<Int>) : Int {
        return board.sumOf { it.filter { !calledNumbers.contains(it) }.sum() }
    }

    private fun rotatedBoard() : List<List<Int>> {
        val rotated = MutableList(board.size) { mutableListOf<Int>()}
        board.forEach { it.forEachIndexed { index, i -> rotated[index].add(i) } }
        return rotated
    }
}