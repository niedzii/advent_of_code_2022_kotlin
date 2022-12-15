package day15

import java.io.File
import kotlin.math.abs
import kotlin.math.absoluteValue

fun main() {
    val input = File("src/day15", "day15_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 4665948
    println("Stage 2 answer is ${stageTwo(input)}") // 13543690671045
}

fun stageOne(input: List<String>): Int {
    val sensors = input.map { it.parseLine() }
    val row = 2000000

    val x = (-2000000..5000000).filter {
        !(row to it).isValid(sensors) && !(it to row).isBeacon(sensors)
    }
    return x.count()
}

fun stageTwo(input: List<String>): Long {
    val sensors = input.map { it.parseLine() }
    val from = 0
    val to = 4_000_000

    val availablePoints = sensors.flatMap { it.getAllPoLongs1TileFromReach() }
        .filter { it.first in (from..to) && it.second in (from..to) }
        .filter { it.isValid(sensors) }
        .distinct()

    if (availablePoints.size != 1) error("Pain")

    return availablePoints.first().let { it.first + it.second * 4_000_000L }
}

private fun Pair<Int, Int>.isValid(sensors: List<Sensor>): Boolean {
    return sensors.all {
        (this.first - it.y).absoluteValue + (this.second - it.x).absoluteValue > it.distanceToBeacon()
    }
}

private fun Pair<Int, Int>.isBeacon(sensors: List<Sensor>) =
    sensors.any { this.first == it.beaconX && this.second == it.beaconY }


private fun String.parseLine(): Sensor {
    val values =
        """Sensor at x=(.+), y=(.+): closest beacon is at x=(.+), y=(.+)""".toRegex().find(this)!!.groupValues.drop(1)
            .map { it.toInt() }
    return Sensor(values[0], values[1], values[2], values[3])
}

private data class Sensor(val x: Int, val y: Int, val beaconX: Int, val beaconY: Int) {
    fun distanceToBeacon() = abs(x - beaconX) + abs(y - beaconY)
    fun getAllPoLongs1TileFromReach(): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        val distance = distanceToBeacon()

        (0..distance).forEach {
            val left = this.x - (distance - it) - 1
            val right = this.x + (distance - it) + 1

            result.add(this.y + it to left)
            result.add(this.y + it to right)
            result.add(this.y - it to left)
            result.add(this.y - it to right)
        }
        result.add(this.y + 1 + distance to this.x)
        result.add(this.y - 1 - distance to this.x)
        return result
    }
}