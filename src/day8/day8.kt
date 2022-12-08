package day8

import java.io.File

fun main() {
    val input = File("src/day8", "day8_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 1690
    println("Stage 2 answer is ${stageTwo(input)}") // 535680
}

fun stageOne(input: List<String>): Int {
    val forest: List<List<Int>> = input.map { it.toCharArray().map { it.digitToInt() } }
    val outerMostTrees = forest.size * 2 + forest.first().size * 2 - 4 // compensate intersections
    var visibleTrees = outerMostTrees

    //only count inner trees
    (1 until forest.size - 1).forEach { x ->
        (1 until forest.first().size - 1).forEach { y ->
            if (isVisible(x, y, forest)) {
                visibleTrees++
            }
        }
    }

    return visibleTrees
}

fun stageTwo(input: List<String>): Int {
    val forest: List<List<Int>> = input.map { it.toCharArray().map { it.digitToInt() } }
    var maxScore = -1

    calculateScore(1, 3, forest)

    (0 until forest.size - 1).forEach { x ->
        (0 until forest.first().size - 1).forEach { y ->
            calculateScore(x, y, forest).takeIf { it > maxScore }?.let { maxScore = it }
        }
    }

    return maxScore
}


private fun isVisible(x: Int, y: Int, forest: List<List<Int>>): Boolean {
    val horizontalRow = forest[y]
    val verticalRow = forest.map { it[x] }

    return isVisibleInRow(horizontalRow, x) ||
            isVisibleInRow(verticalRow, y)
}

private fun isVisibleInRow(row: List<Int>, index: Int) : Boolean {
    val oneSide = row.subList(0, index).maxOrNull()!! < row[index]
    val reversedIndex = row.size - index - 1
    val otherSide = row.asReversed().subList(0, reversedIndex).maxOrNull()!! < row[index]
    return oneSide || otherSide
}

private fun calculateScore(x: Int, y: Int, forest: List<List<Int>>) : Int {
    val currentHeight = forest[y][x]
    val horizontalRow = forest[y]
    val verticalRow = forest.map { it[x] }

    val rowToLeft = verticalRow.subList(0, y).reversed()
    val rowToRight = verticalRow.subList(y + 1, verticalRow.size)
    val rowUp = horizontalRow.subList(0, x).reversed()
    val rowDown = horizontalRow.subList(x + 1, horizontalRow.size)

    return rowToLeft.visibleTrees(currentHeight) * rowToRight.visibleTrees(currentHeight) * rowUp.visibleTrees(currentHeight) * rowDown.visibleTrees(currentHeight)
}

private fun List<Int>.visibleTrees(height: Int) : Int {
    val visibleTrees = this.takeWhile { it < height }.count()
    if(visibleTrees == this.size) return visibleTrees // if there is no more trees don't add one blocking view
    return visibleTrees + 1
}