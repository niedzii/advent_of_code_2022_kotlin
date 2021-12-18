package day15

import java.io.File
import java.lang.IndexOutOfBoundsException
import java.util.PriorityQueue

val DIRECTIONS = listOf(
    -1 to 0,
    0 to -1,
    1 to 0,
    0 to 1
)

fun main() {
    val input = File("src/day15", "day15_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 621
    println("Stage 2 answer is ${stageTwo(input)}") // 2904
}

fun stageOne(input: List<String>): Int {
    val map = input.map { row -> row.toCharArray().map { it.digitToInt() } }
    return findLowestRiskPath(map)
}

fun stageTwo(input: List<String>): Int {
    val map = input.map { row -> row.toCharArray().map { it.digitToInt() } }
    return findLowestRiskPath(createNthTimesLargerMap(map, 5))

}

private fun createNthTimesLargerMap(
    map: List<List<Int>>,
    n : Int
) : List<List<Int>>{
    val resultMap = MutableList<MutableList<Int>>(map.size * (n - 1)) { mutableListOf()}
    map.forEachIndexed { index, list ->
        resultMap.add(mutableListOf())
        //first multiply whole row
        for (i in 0 until n) {
            resultMap[index].addAll(addRiskToElements(list, i))
        }

        // add new multiplied rows
        for (j in 1 until n) {
            resultMap[index + j * map.size].addAll(addRiskToElements(resultMap[index], j))
        }
    }
    return resultMap
}

private fun findLowestRiskPath(map: List<List<Int>>): Int {
    val visited = mutableSetOf<Pair<Int, Int>>()

    val queue: PriorityQueue<Pair<Int, Pair<Int, Int>>> =
        // Pair<cost_to_get_here, Pair<x_coord, y_coord>>
        PriorityQueue<Pair<Int, Pair<Int, Int>>> { a, b -> a.first - b.first }

    queue.add(0 to (0 to 0))

    while (queue.isNotEmpty()) {
        val element = queue.poll()
        for (neighbour in getNeighbours(element.second, map)) {

            if (visited.contains(neighbour)) continue

            if (neighbour.first == map.size - 1 && neighbour.second == map[0].size - 1) {
                return element.first + map[neighbour.first][neighbour.second]
            }
            queue.add(element.first + map[neighbour.first][neighbour.second] to neighbour)
            visited.add(neighbour)
        }
    }

    return 0
}

private fun getNeighbours(position: Pair<Int, Int>, map: List<List<Int>>): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()

    DIRECTIONS.forEach {
        try {
            map[position.first + it.first][position.second + it.second]
            result.add(position.first + it.first to position.second + it.second)
        } catch (e: IndexOutOfBoundsException) {
        }
    }
    return result
}

private fun addRiskToElements(row: List<Int>, riskToAdd: Int): List<Int> {
    return row.map {
        var toAdd = it + riskToAdd
        while (toAdd > 9) toAdd -= 9
        toAdd
    }
}