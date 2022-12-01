package day1

import java.io.File

fun main() {
    val calories = File("src/day1", "day1_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(calories)}")
    println("Stage 2 answer is ${stageTwo(calories)}")
}

fun stageOne(calories: List<String>): Int {
    val list = extractCaloriesPerElf(calories)
    return list.maxOrNull()!!
}

fun stageTwo(calories: List<String>): Int {
    val list = extractCaloriesPerElf(calories)
    list.sortByDescending { it }
    return list.take(3).sum()
}

private fun extractCaloriesPerElf(calories: List<String>): MutableList<Int> {
    val list = mutableListOf<Int>()
    var temp = 0
    for (calory in calories) {
        if (calory == "") {
            list.add(temp)
            temp = 0
        } else {
            temp += calory.toInt()
        }
    }
    return list
}