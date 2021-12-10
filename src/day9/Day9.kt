package day9

import java.io.File
import java.lang.Character.getNumericValue

fun main() {
    val input = File("src/day9", "day9_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 572
    println("Stage 2 answer is ${stageTwo(input)}")
}

fun stageOne(input: List<String>): Int {
    val height = input.size
    val width = input[0].length

    var result = 0

    input.forEachIndexed { index, it ->
        for (i in 0 until width) {
            var previous = 10
            var next = 10
            val current = getNumericValue(it[i])
            if (i - 1 >= 0) {
                previous = getNumericValue(it[i - 1])
            }

            if (i + 1 < width) {
                next = getNumericValue(it[i + 1])
            }

            if (current < next && current < previous) {

                var upper = 10
                var lower = 10

                if (index != 0) {
                    upper = getNumericValue(input[index - 1][i])
                }

                if (index != height - 1) {
                    lower = getNumericValue(input[index + 1][i])
                }

                if (current < upper && current < lower) {
                    result += (current + 1)
                }

            }
        }
    }
    return result
}

fun stageTwo(input: List<String>): Int {
    TODO("Implement later")
    return 0
}
