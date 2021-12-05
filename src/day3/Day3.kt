package day3

import java.io.File
import java.lang.Character.getNumericValue
import kotlin.math.pow

fun main() {
    val readings = File("src/day3", "day3_input.txt").readLines().map { it.trim() }
    println("Stage 1 answer is ${stageOne(readings)}") // 741950
    println("Stage 2 answer is ${stageTwo(readings)}") // 903810
}

fun stageOne(readings: List<String>): Int {
    val countedBits = countEachBit(readings)

    val gamma = binaryListToDecimal(countedBits.map { if (it > readings.size / 2) 1 else 0 })
    val epsilon = binaryListToDecimal(countedBits.map { if (it < readings.size / 2) 1 else 0 })
    return gamma * epsilon
}

fun stageTwo(readings: List<String>): Int {
    val oxygenRating = getOxygenRating(readings)
    val co2scrubberRating = getCo2ScrubberRating(readings)

    return oxygenRating * co2scrubberRating
}

fun getOxygenRating(readings: List<String>): Int {
    val readingsAsNumbers : MutableList<List<Int>> = readings.map { line -> line.toCharArray().map { getNumericValue(it.code) } }.toMutableList()

    var index = 0
    while(readingsAsNumbers.size != 1) {
        val sumOnBit = sumOnGivenBit(readingsAsNumbers, index)
        val numberOfBits = readingsAsNumbers.size
        val numberOfZeros = numberOfBits - sumOnBit
        val numberOfOnes = numberOfBits - numberOfZeros

        if(numberOfOnes >= numberOfZeros) {
            readingsAsNumbers.removeIf { it[index] == 0 }
        } else {
            readingsAsNumbers.removeIf { it[index] == 1 }
        }
        index++
    }

    return binaryListToDecimal(readingsAsNumbers[0])
}

fun getCo2ScrubberRating(readings: List<String>): Int {
    val readingsAsNumbers : MutableList<List<Int>> = readings.map { line -> line.toCharArray().map { getNumericValue(it.code) } }.toMutableList()

    var index = 0
    while(readingsAsNumbers.size != 1) {
        val sumOnBit = sumOnGivenBit(readingsAsNumbers, index)
        val numberOfBits = readingsAsNumbers.size
        val numberOfZeros = numberOfBits - sumOnBit
        val numberOfOnes = numberOfBits - numberOfZeros

        if(numberOfOnes < numberOfZeros) {
            readingsAsNumbers.removeIf { it[index] == 0 }
        } else {
            readingsAsNumbers.removeIf { it[index] == 1 }
        }
        index++
    }

    return binaryListToDecimal(readingsAsNumbers[0])
}

fun sumOnGivenBit(list: List<List<Int>>, index: Int) : Int {
    return list.map { it[index] }.sum()
}

private fun countEachBit(readings: List<String>) : List<Int> {
    val result = MutableList(readings[0].length) { 0 }

    readings.forEach { line ->
        line.toCharArray().withIndex().forEach {
            result[it.index] += getNumericValue(it.value.code)
        }
    }

    return result
}

fun binaryListToDecimal(binaryList:List<Int>) : Int {
    var result = 0.0
    binaryList.asReversed().forEachIndexed { index, i ->
        result += i * 2.0.pow(index) }

    return result.toInt()
}
