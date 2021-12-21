package day21

import java.io.File
import kotlin.math.max

private const val STAGE_ONE_POINT_THRESHOLD = 1000
private const val STAGE_TWO_POINT_THRESHOLD = 21

fun main() {
    val input = File("src/day21", "day21_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 713328
    println("Stage 2 answer is ${stageTwo(input)}") // 92399285032143
}

fun stageOne(input: List<String>): Int {
    var playerOneScore = 0; var playerTwoScore = 0
    var (playerOnePosition, playerTwoPosition) = input.map { it.substringAfter("position:").trim().toInt() - 1 }.zipWithNext()[0]

    var diceRolls = 0
    var currentDice = 0

    while (true) {
        var rolledThreeTimes = rollThreeTimes(currentDice)
        currentDice = rolledThreeTimes.first
        diceRolls += rolledThreeTimes.second

        var nextField = calculateFieldAfterMoves(playerOnePosition, rolledThreeTimes.third)

        playerOneScore += nextField + 1
        playerOnePosition = nextField
        if (playerOneScore >= STAGE_ONE_POINT_THRESHOLD) {
            return playerTwoScore * diceRolls
        }

        rolledThreeTimes = rollThreeTimes(currentDice)
        currentDice = rolledThreeTimes.first
        diceRolls += rolledThreeTimes.second

        nextField = calculateFieldAfterMoves(playerTwoPosition, rolledThreeTimes.third)

        playerTwoScore += nextField + 1
        playerTwoPosition = nextField
        if (playerTwoScore >= STAGE_ONE_POINT_THRESHOLD) {
            return playerOneScore * diceRolls
        }
    }
}

fun stageTwo(input: List<String>): Long {
    val (playerOnePosition, playerTwoPosition) = input.map { it.substringAfter("position:").trim().toInt() - 1 }.zipWithNext()[0]
    val countWin = countWin(playerOnePosition, playerTwoPosition, 0, 0)

    return max(countWin.first, countWin.second)
}

private fun calculateFieldAfterMoves(playerPosition: Int, fieldsAhead: Int) =
    (playerPosition + fieldsAhead) % 10

private fun rollThreeTimes(
    currentDice: Int,
): Triple<Int, Int, Int> {
    var currentDice1 = currentDice
    var diceRolls = 0
    var fieldsAhead = 0
    for (i in 1..3) {
        currentDice1 = rollDice(currentDice1)
        diceRolls++
        fieldsAhead += currentDice1
    }
    return Triple(currentDice1, diceRolls, fieldsAhead)
}

private fun rollDice(currentDice: Int): Int {
    return if (currentDice == 100) 1 else currentDice + 1
}

data class State(
    val playerOnePosition: Int,
    val playerTwoPosition: Int,
    val playerOneScore: Int,
    val playerTwoScore: Int
)

val cache = mutableMapOf<State, Pair<Long, Long>>()

fun countWin(
    playerOnePosition: Int,
    playerTwoPosition: Int,
    playerOneScore: Int,
    playerTwoScore: Int
): Pair<Long, Long> {

    if (playerOneScore >= STAGE_TWO_POINT_THRESHOLD) return 1L to 0L
    if (playerTwoScore >= STAGE_TWO_POINT_THRESHOLD) return 0L to 1L

    val cached = cache[(State(playerOnePosition, playerTwoPosition, playerOneScore, playerTwoScore))]
    if (cached != null) return cached

    var result = 0L to 0L

    for (d1 in 1..3) {
        for (d2 in 1..3) {
            for (d3 in 1..3) {
                val newPlayerOnePosition = (playerOnePosition + d1 + d2 + d3) % 10
                val newPlayerOneScore = playerOneScore + newPlayerOnePosition + 1

                val (nextTurnPlayerOneUniverseWins, nextTurnPlayerTwoUniverseWins) = countWin(
                    playerTwoPosition,
                    newPlayerOnePosition,
                    playerTwoScore,
                    newPlayerOneScore
                )

                result =
                    (result.first + nextTurnPlayerTwoUniverseWins) to (result.second + nextTurnPlayerOneUniverseWins)
            }
        }
    }

    cache[State(playerOnePosition, playerTwoPosition, playerOneScore, playerTwoScore)] = result

    return result
}