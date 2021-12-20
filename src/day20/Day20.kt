package day20

import java.io.File
import kotlin.math.pow

fun main() {
    val input = File("src/day20", "day20_input.txt").readLines()
    val algorithm = input[0]
    val image = input.subList(2, input.size).map { it.toCharArray().toList() }
    println("Stage 1 answer is ${calculateLitPixelsAfterSteps(algorithm, image, 2, 5)}") // 5249
    println("Stage 2 answer is ${calculateLitPixelsAfterSteps(algorithm, image, 50, 55)}") // 15714
}

fun calculateLitPixelsAfterSteps(algorithm: String, image: List<List<Char>>, steps : Int, additionalSize: Int): Int {
    var extendedImage = createExtendedImage(additionalSize, image)

    for (i in 0 until steps) {
        val nextFrame = mutableListOf<MutableList<Char>>()
        val charsOutsideOfGrid = if (i % 2 == 0) '.' else '#'

        extendedImage.forEachIndexed { rowIndex, row ->
            nextFrame.add(mutableListOf())
            row.forEachIndexed { columnIndex, _ ->
                val neighbours = getNeighbours(rowIndex to columnIndex, extendedImage, charsOutsideOfGrid)
                val neighboursValue = neighbours.binaryToDecimal()
                nextFrame[rowIndex].add(algorithm[neighboursValue])
             }
        }
        extendedImage = nextFrame
    }
    return extendedImage.flatten().count { it == '#' }
}

private fun createExtendedImage(
    additionalSize: Int,
    image: List<List<Char>>
): MutableList<MutableList<Char>> {
    val extendedImage = mutableListOf<MutableList<Char>>()
    repeat((0 until additionalSize).count()) {
        extendedImage.add(MutableList(additionalSize * 2 + image[0].size) { '.' })
    }

    image.forEach {
        val rowResult = mutableListOf<Char>()
        rowResult.addAll(MutableList(additionalSize) { '.' })
        rowResult.addAll(it)
        rowResult.addAll(MutableList(additionalSize) { '.' })
        extendedImage.add(rowResult)
    }


    repeat((0 until additionalSize).count()) {
        extendedImage.add(MutableList(additionalSize * 2 + image[0].size) { '.' })
    }
    return extendedImage
}

private fun String.binaryToDecimal(): Int {
    val reversed = this.map { if (it == '.') 0 else 1 }.reversed()
    var result = 0

    reversed.forEachIndexed { index, c ->
        result += (2.0.pow(index) * c).toInt()
    }
    return result
}

private fun getNeighbours(cords: Pair<Int, Int>, image: List<List<Char>>, charsOutsideOfGrid: Char): String {
    var result = ""
    DIRECTIONS.forEach {
        val charToAdd = try {
            image[cords.first + it.first][cords.second + it.second]
        } catch (e : IndexOutOfBoundsException) {
            charsOutsideOfGrid
        }
        result += charToAdd
    }
    return result
}

val DIRECTIONS = listOf(
    // upper row
    -1 to -1,
    -1 to 0,
    -1 to 1,
    // middle row
    0 to -1,
    0 to 0,
    0 to 1,
    // lower row
    1 to -1,
    1 to 0,
    1 to 1,
)
