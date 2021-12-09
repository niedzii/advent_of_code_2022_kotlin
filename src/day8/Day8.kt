package day8

import java.io.File

fun main() {
    val input = File("src/day8", "day8_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 543
    println("Stage 2 answer is ${stageTwo(input)}") // 994266
}

fun stageOne(input: List<String>): Int {
    val numbers = intArrayOf(2, 4, 3, 7)
    return input.map { line -> line.split("|")[1].trim().split(" ").map { it.length } }.flatMap { it.asIterable() }
        .count { numbers.contains(it) }
}

fun stageTwo(input: List<String>): Int {
    val line = input.map { it.replace(" |", "") }.map { it.split(" ") }
    var result = 0
    line.forEach {

        val map = mutableMapOf<Int, String>()
        map[1] = it.find { it.length == 2 }!!
        map[4] = it.find { it.length == 4 }!!
        map[7] = it.find { it.length == 3 }!!
        map[8] = it.find { it.length == 7 }!!

        it.forEach { segment ->
            if(segment.length == 5) {
                if(lettersMatch(segment, map[1]!!) == 2) map[3] = segment
                else if (lettersMatch(segment, map[4]!!) == 3) map[5] = segment
                else map[2] = segment
            } else if (segment.length == 6) {
                if(lettersMatch(segment, map[1]!!) == 1) map[6] = segment
                else if (lettersMatch(segment, map[4]!!) == 4) map[9] = segment
                else map[0] = segment
            }
        }

        val firstNumber = map.entries.find { entry -> containsAll(entry.value, it[10])}!!.key
        val secondNumber = map.entries.find { entry -> containsAll(entry.value, it[11])}!!.key
        val thirdNumber = map.entries.find { entry -> containsAll(entry.value, it[12])}!!.key
        val fourthNumber = map.entries.find { entry -> containsAll(entry.value, it[13])}!!.key
        result += firstNumber * 1000 + secondNumber * 100 + thirdNumber * 10 + fourthNumber
    }

    return result
}

private fun containsAll(first:String, second:String) : Boolean {
    return first.toList().containsAll(second.toList()) && first.length == second.length
}

private fun lettersMatch(first: String, second: String): Int {
    val secondChars = second.toCharArray()
    return first.toCharArray().count { secondChars.contains(it) }
}