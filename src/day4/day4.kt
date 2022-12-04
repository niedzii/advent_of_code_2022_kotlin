package day4

import java.io.File

fun main() {
    val input = File("src/day4", "day4_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 542
    println("Stage 2 answer is ${stageTwo(input)}") // 900
}

fun stageOne(input: List<String>): Int {
    return input.map { it.split(",") }.map { it[0] to it[1] }
        .map { (it.first.split("-") to it.second.split("-")) }
        .map { (it.first[0].toInt() to it.first[1].toInt()) to (it.second[0].toInt() to it.second[1].toInt()) }
        .count { oneContainsOther(it) }
}

fun stageTwo(input: List<String>): Int {
    return input.map { it.split(",") }.map { it[0] to it[1] }
        .map { (it.first.split("-") to it.second.split("-")) }
        .map { (it.first[0].toInt() to it.first[1].toInt()) to (it.second[0].toInt() to it.second[1].toInt()) }
        .count { oneOverlapsOther(it) }
}

private fun oneContainsOther(elements: Pair<Pair<Int, Int>, Pair<Int, Int>>): Boolean {
    val firstElement = elements.first
    val secondElement = elements.second

    return firstElement.contains(secondElement) || secondElement.contains(firstElement)
}

private fun Pair<Int, Int>.contains(other: Pair<Int, Int>) = this.first <= other.first && this.second >= other.second

private fun oneOverlapsOther(elements: Pair<Pair<Int, Int>, Pair<Int, Int>>): Boolean {
    val firstElement = elements.first
    val secondElement = elements.second

    if(firstElement.first > secondElement.second) return false

    return firstElement.second >= secondElement.first

}

