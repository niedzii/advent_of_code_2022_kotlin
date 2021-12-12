package day12

import java.io.File
import java.util.LinkedList

fun main() {
    val input = File("src/day12", "day12_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 3230
    println("Stage 2 answer is ${stageTwo(input)}") // 83475
}

fun stageOne(input: List<String>): Int {
    val paths = createAdjacencyList(input)

    val queue = LinkedList<Pair<String, MutableSet<String>>>()
    queue.add("start" to mutableSetOf("start"))
    var result = 0

    while (queue.isNotEmpty()) {
        val element = queue.pop()
        for (nodeToGo in paths.getOrDefault(element.first, emptyList())) {
            if (nodeToGo == "end") {
                result++
            } else {
                if (nodeToGo.isUpperCase() || !element.second.contains(nodeToGo)) {
                    val copy = element.second.toMutableSet()
                    copy.add(nodeToGo)
                    queue.add(nodeToGo to copy)
                }
            }
        }
    }
    return result
}

fun stageTwo(input: List<String>): Int {
    val paths = createAdjacencyList(input)

    val queue = LinkedList<Triple<String, MutableList<String>, Boolean>>()
    // prevents from going back to start
    queue.add(Triple("start", mutableListOf("start", "start", "end"), false))
    var result = 0

    while (queue.isNotEmpty()) {
        val element = queue.pop()
        for (nodeToGo in paths.getOrDefault(element.first, emptyList())) {
            if (nodeToGo == "end") {
                result++
                continue
            }

            if (nodeToGo.isUpperCase() ||
                (!element.third && nodeToGo != "start" && nodeToGo != "end") ||
                !element.second.contains(nodeToGo)
            ) {
                // if not visited extra cave and is visiting extra cave now
                val third = element.third || nodeToGo.isLowerCase() && element.second.contains(nodeToGo)
                val newVisitedCaves = element.second.toMutableList()
                newVisitedCaves.add(nodeToGo)
                queue.add(Triple(nodeToGo, newVisitedCaves, third))
            }
        }
    }
    return result
}

fun String.isLowerCase(): Boolean {
    return this.lowercase() == this
}

fun String.isUpperCase(): Boolean {
    return this.uppercase() == this
}

private fun createAdjacencyList(
    input: List<String>
): MutableMap<String, MutableList<String>> {
    val paths = mutableMapOf<String, MutableList<String>>()
    input.map { it.split("-") }.forEach {
        paths.computeIfAbsent(it[1]) { mutableListOf() }.add(it[0])
        paths.computeIfAbsent(it[0]) { mutableListOf() }.add(it[1])
    }
    return paths
}
