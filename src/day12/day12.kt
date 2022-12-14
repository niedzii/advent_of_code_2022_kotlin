package day12

import java.io.File
import java.lang.Exception

fun main() {
    val input = File("src/day12", "day12_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 468
//    println("Stage 2 answer is ${stageTwo(input)}") // 459
}

fun stageOne(input: List<String>): Int {
    val map = input.map { it.trim() }.map { it.toCharArray() }
    val startX = map.indexOf(map.find { it.contains('S') })
    val start = startX to map[startX].indexOf('S')

    return calculateShortestPath(map, start)
}

fun stageTwo(input: List<String>): Int {
    val map = input.map { it.trim() }.map { it.toCharArray() }
    val possibleStarts = mutableListOf<Pair<Int, Int>>()

    map.forEachIndexed { y, chars ->
        chars.forEachIndexed { x, tile ->
            if (tile == 'a') possibleStarts.add(y to x)
        }
    }
    return possibleStarts.map { calculateShortestPath(map, it) }.minOf { it }
}

private fun calculateShortestPath(
    map: List<CharArray>,
    start: Pair<Int, Int>
): Int {
    val startY = map.indexOf(map.find { it.contains('E') })
    val deque = ArrayDeque(listOf(Pathway(start, 0)))
    val visited = mutableListOf(start)
    val finish = startY to map[startY].indexOf('E')

    while (true) {
        try {
            val current = deque.removeFirst()

            if (current.current == finish) {
                return current.score
            }

            current.current.getNeighbours(map).filterNot { visited.contains(it) }.forEach {
                visited.add(it)
                deque.add(Pathway(it, current.score + 1))
            }
        } catch (e: Exception) {
            return Integer.MAX_VALUE
        }

    }
}


private data class Pathway(
    val current: Pair<Int, Int>,
    val score: Int
)

private fun Pair<Int, Int>.getNeighbours(map: List<CharArray>): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    val options = getOptions()
    options.forEach {
        try {
            val temp = this.add(it)

            map[temp.first][temp.second]

            if (this.canGo(temp, map)) result.add(temp)

        } catch (_: IndexOutOfBoundsException) {
        }
    }
    return result
}

private fun Pair<Int, Int>.canGo(to: Pair<Int, Int>, map: List<CharArray>): Boolean {
    if (map[to.first][to.second] == 'E') {
        return 'z'.code - map[this.first][this.second].code < 2
    }
    if (map[this.first][this.second] == 'S') {
        return map[to.first][to.second].code - 'a'.code < 2
    }
    return map[to.first][to.second].code - map[this.first][this.second].code < 2
}

private fun getOptions() = listOf(
    0 to 1,
    1 to 0,
    0 to -1,
    -1 to 0,
)

private fun Pair<Int, Int>.add(pair: Pair<Int, Int>) =
    this.copy(first = this.first + pair.first, second = this.second + pair.second)
