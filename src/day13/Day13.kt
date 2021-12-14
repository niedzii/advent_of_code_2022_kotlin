package day13

import java.io.File
import java.util.LinkedList

fun main() {
    val input = File("src/day13", "day13_input.txt").readLines().toMutableList()
    input.remove("")
    val completedFold = stageOne(input)
    println("Stage 1 answer is ${completedFold.size}") // 847
    stageTwo(completedFold)
    println("Stage 2 answer is BCZRCEAB")
}

fun stageOne(input: List<String>): Set<Pair<Int, Int>> {
    val (foldDirection, rows) = input.partition { it.startsWith("fold along") }

    var mutableRows = rows.map {
        val split = it.split(",")
        Pair(split[0].toInt(), split[1].toInt())
    }.toMutableSet()
    foldDirection.forEach {
        val direction = it.split("=")[0].takeLast(1)
        val value = it.split("=")[1].toInt()
        mutableRows = if (direction == "x") mutableRows.foldOnX(value) else mutableRows.foldOnY(value)
    }
    return mutableRows
}


fun MutableSet<Pair<Int, Int>>.foldOnX(x: Int): MutableSet<Pair<Int, Int>> {
    val newSet = mutableSetOf<Pair<Int, Int>>()
    this.forEach {
        if (it.first > x) {
            val distanceToFold = it.first - x
            newSet.add(x - distanceToFold to it.second)
        } else {
            newSet.add(it)
        }
    }
    return newSet
}

fun MutableSet<Pair<Int, Int>>.foldOnY(y: Int): MutableSet<Pair<Int, Int>> {
    val newSet = mutableSetOf<Pair<Int, Int>>()
    this.forEach {
        if (it.second > y) {
            val distanceToFold = it.second - y
            newSet.add(it.first to y - distanceToFold)
        } else {
            newSet.add(it)
        }
    }
    return newSet
}

fun stageTwo(rows: Set<Pair<Int, Int>>) {
    for (y in 0..5) {
        for (x in 0..39) {
            print(if (rows.contains(Pair(x, y))) "#" else " ")
        }
        println()
    }
}
