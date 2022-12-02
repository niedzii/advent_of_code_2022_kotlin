package day2

import java.io.File
import java.lang.RuntimeException

fun main() {
    val input = File("src/day2", "day2_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 11906
    println("Stage 2 answer is ${stageTwo(input)}") // 11186
}

fun stageOne(input: List<String>): Int {
    return input.map { it[0] to it[2] }.map { pointsPerRound(it) }.sum()
}

fun stageTwo(input: List<String>): Int {
    return input.map { it[0] to it[2] }.map { mapAfterSantasPlay(it) }.map { pointsPerRound(it) }.sum()
}

private fun mapAfterSantasPlay(pair: Pair<Char, Char>): Pair<Char, Char> {
    return when (pair.second) {
        'X' -> {
            pair.copy(second = pair.first.toElement().loser().santa)
        }
        'Y' -> {
            pair.copy(second = pair.first.toElement().santa)
        }
        else -> {
            pair.copy(second = pair.first.toElement().winner().santa)
        }
    }
}

private fun pointsPerRound(pair: Pair<Char, Char>): Int {
    return pair.second.toElement().point + outcome(pair)
}

private fun outcome(pair: Pair<Char, Char>): Int {
    return when {
        // draw
        pair.second.toElement() == pair.first.toElement() -> {
            3
        }
        // santa wins
        pair.first.toElement().winner() == pair.second.toElement() -> {
            6
        }
        else -> {
            0
        }
    }
}

private fun Char.toElement() = Element.getElement(this)

private enum class Element(val elf: Char, val santa: Char, val point: Int) {
    ROCK('A', 'X', 1),
    PAPER('B', 'Y', 2),
    SCISSORS('C', 'Z', 3);

    fun winner() = when (this) {
        ROCK -> PAPER
        PAPER -> SCISSORS
        SCISSORS -> ROCK
    }

    fun loser() = when (this) {
        ROCK -> SCISSORS
        PAPER -> ROCK
        SCISSORS -> PAPER
    }

    companion object {
        fun getElement(char: Char) = when (char) {
            'A', 'X' -> ROCK
            'B', 'Y' -> PAPER
            'C', 'Z' -> SCISSORS
            else -> throw RuntimeException()
        }
    }
}