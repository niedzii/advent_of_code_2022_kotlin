package day16

import java.io.File
import kotlin.math.max

private val FIRST_CACHE = mutableMapOf<State, Int>()
private val SECOND_CACHE = mutableMapOf<State, Int>()

fun main() {
    val input = File("src/day16", "day16_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 1751
    println("Stage 2 answer is ${stageTwo(input)}") // 2207
}

fun stageOne(input: List<String>): Int {
    val root = extract(input).find { it.name == "AA" }!!
    val time = 30
    return calculate(root, root, mutableSetOf(), time - 1, FIRST_CACHE, 0)
}

fun stageTwo(input: List<String>): Int {
    val root = extract(input).find { it.name == "AA" }!!
    val time = 26
    return calculate(root, root, mutableSetOf(), time - 1, FIRST_CACHE, 1)
}

private fun calculate(
    root: Valve,
    currentNode: Valve,
    openedValved: MutableSet<Valve>,
    minutesLeft: Int,
    cache: MutableMap<State, Int>,
    remainingPlayers: Int
): Int {

    val ele = State(currentNode, openedValved, minutesLeft)

    if (cache.contains(ele)) {
        return cache[ele]!!
    }

    var top = 0

    if (ele.minutesLeft == 0) {
        return if (remainingPlayers > 0) calculate(root, root, openedValved, 25, SECOND_CACHE, 0) else 0
    }

    if (ele.currentNode.rate > 0 && !ele.openedValved.contains(ele.currentNode)) {
        // open
        top = max(
            top, ele.currentNode.rate * ele.minutesLeft + calculate(
                root,
                currentNode,
                ele.openedValved.toMutableSet().also { it.add(ele.currentNode) },
                ele.minutesLeft - 1,
                cache,
                remainingPlayers
            )
        )
    }

    // move state to neighbours
    ele.currentNode.neighbours.forEach {
        top = max(top, calculate(root, it, ele.openedValved, ele.minutesLeft - 1, cache, remainingPlayers))
    }

    cache[ele] = top

    return top
}

private fun extract(input: List<String>): List<Valve> {
    val regex = "Valve (.+) has flow rate=(\\d+)".toRegex()

    val (valves, connections) = input.map { it.split(";") }
        .map { regex.matchEntire(it[0])!!.groupValues to it[1] }
        .map { Valve(it.first[1], it.first[2].toInt()) to it.second }
        .unzip()


    (valves.indices).forEach { index ->
        val valve = valves[index]
        val connections = connections[index].split(" ").map { it.replace(",", "") }.drop(5)

        connections.forEach { conn ->
            valve.addNeighbours(valves.filter { it.name == conn })
        }

    }

    return valves
}

private class Valve(val name: String, val rate: Int, val neighbours: MutableList<Valve> = mutableListOf()) {
    fun addNeighbours(new: List<Valve>) = neighbours.addAll(new)
}

private data class State(
    val currentNode: Valve,
    val openedValved: MutableSet<Valve>,
    val minutesLeft: Int
)