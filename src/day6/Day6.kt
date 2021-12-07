package day6

import java.io.File

fun main() {
    val input = File("src/day6", "day6_input.txt").readLines().flatMap { it.split(",") }.map { it.toLong() }

    println("Stage 1 answer is ${stageOne(input)}") // 389726
    println("Stage 2 answer is ${stageTwo(input)}") // 1743335992042
}

fun stageOne(input: List<Long>): Long {
    return calculateNumberOfFishInDays(input, 80)
}

fun stageTwo(input: List<Long>): Long {
    return calculateNumberOfFishInDays(input, 256)
}

private fun calculateNumberOfFishInDays(
    input: List<Long>,
    numberOfDays: Int
): Long {
    // days, quantity
    val pairs = input.groupBy { it }.entries.map { mutableListOf(it.key, it.value.count().toLong()) }.toMutableList()

    for (day in 1..numberOfDays) {
        pairs.forEach { it[0] = it[0] - 1 }

        val find = pairs.find { it[0] == -1L }

        if (find != null) {
            val findSix = pairs.find { it[0] == 6L }
            if (findSix == null) find[0] = 6 else {
                findSix[1] = findSix[1] + find[1]
                pairs.remove(find)
            }

            pairs.add(mutableListOf(8, find[1]))
        }

    }
    return pairs.sumOf { it[1] }
}
