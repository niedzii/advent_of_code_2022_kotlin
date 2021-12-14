package day14

import java.io.File

fun main() {
    val input = File("src/day14", "day14_input.txt").readLines().toMutableList()
    input.remove("")
    println("Stage 1 answer is ${stageOne(input)}") // 2509
    println("Stage 2 answer is ${stageTwo(input)}") // 2827627697643
}

fun stageOne(input: List<String>): Long {
    return calculateDifferenceBetweenMostAndLeastUsedElement(input, 10)
}

fun stageTwo(input: List<String>): Long {
    return calculateDifferenceBetweenMostAndLeastUsedElement(input, 40)
}

private fun calculateDifferenceBetweenMostAndLeastUsedElement(input: List<String>, steps: Int): Long {
    val startValue = input[0]
    val instructions = parseInstructions(input)

    var sequences: Map<Pair<Char, Char>, Long> =
        startValue.zipWithNext().groupBy { it }.mapValues { it.value.size.toLong() }

    // [A,C] with rule 'AC -> B' will result in [AB][BC] so to calculate correct number of elements there is need to keep track of duplicated elements
    val duplicateElements = mutableMapOf<Char, Long>()

    // add duplicated values to start value as well ABC -> [AB][BC] so B is duplicated
    startValue.subSequence(1, startValue.length - 1).forEach {
        duplicateElements[it] = (duplicateElements[it] ?: 0) - 1
    }

    for (i in 0 until steps) {
        val currentIterationSequences = mutableMapOf<Pair<Char, Char>, Long>()

        instructions.forEach {
            val numberOfElements = sequences[it.first[0] to it.first[1]]

            if (numberOfElements != null) {

                val leftPair = Pair(it.first[0], it.second[0])
                val rightPair = Pair(it.second[0], it.first[1])

                // add left pair
                currentIterationSequences[leftPair] =
                    (currentIterationSequences[leftPair] ?: 0) + numberOfElements

                // add right pair
                currentIterationSequences[rightPair] =
                    (currentIterationSequences[rightPair] ?: 0) + numberOfElements

                // keep track of duplicates
                duplicateElements[it.second[0]] = (duplicateElements[it.second[0]] ?: 0) - numberOfElements
            }
        }
        // it has to be calculated for next frame not to take into account added this turn
        sequences = currentIterationSequences
    }

    // merge sequences with duplicate elements to calculated quantity of those
    sequences.forEach {
        duplicateElements[it.key.first] = (duplicateElements[it.key.first] ?: 0) + it.value
        duplicateElements[it.key.second] = (duplicateElements[it.key.second] ?: 0) + it.value
    }

    return duplicateElements.maxByOrNull { it.value }!!.value -  duplicateElements.minByOrNull { it.value }!!.value
}

private fun parseInstructions(input: List<String>) = input.subList(1, input.size).map {
    val split = it.split(" -> ")
    split[0] to split[1]
}
