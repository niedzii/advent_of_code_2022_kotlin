package day2

import java.io.File

fun main() {
    val commands = File("src/day2", "day2_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(commands)}") // 1924923
    println("Stage 2 answer is ${stageTwo(commands)}") // 1982495697
}

fun stageOne(commands: List<String>): Int {
    var xAxis = 0
    var yAxis = 0

    commands.map { it.trim() }.forEach {
        val split = it.split(" ")
        val direction = split[0]
        val value = split[1].toInt()
        when (direction) {
            "forward" -> xAxis += value
            "down" -> yAxis += value
            "up" -> yAxis -= value
        }
    }
    return xAxis * yAxis
}

fun stageTwo(commands: List<String>): Int {
    var aim = 0
    var xAxis = 0
    var yAxis = 0

    commands.map { it.trim() }.forEach {
        val split = it.split(" ")
        val direction = split[0]
        val value = split[1].toInt()
        when (direction) {
            "forward" -> {
                xAxis += value
                yAxis += (aim * value)
            }
            "down" -> aim += value
            "up" -> aim -= value
        }
    }
    return xAxis * yAxis
}