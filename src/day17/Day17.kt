package day17

import java.io.File

fun main() {
    val input = File("src/day17", "day17_input.txt").readLines()[0]
    val (leftX, rightX) = input.split("x=")[1].split(",")[0].split("..").map { it.toInt() }
    val (lowerY, upperY) = input.split("y=")[1].split("..").map { it.toInt() }

    println("Stage 1 answer is ${stageOne(leftX..rightX to lowerY..upperY)}") // 10585
    println("Stage 2 answer is ${stageTwo(leftX..rightX to lowerY..upperY)}") // 5247
}

fun stageOne(target: Pair<IntRange, IntRange>): Int {
    var best = Int.MIN_VALUE
    for (x in 1..157) {
        for (y in -100..500) {
            val (isInTarget, maxY) = calculateIfItWillHitTheTargetAndMaxY(x to y, target)
            if(isInTarget && best < maxY) {
                best = maxY
            }
        }
    }

    return best
}

fun stageTwo(target: Pair<IntRange, IntRange>): Int {
    var count = 0
    for (x in 1..157) {
        for (y in -146..500) {
            val (isInTarget, _) = calculateIfItWillHitTheTargetAndMaxY(x to y, target)
            if (isInTarget) count++
        }
    }
    return count
}

private fun calculateIfItWillHitTheTargetAndMaxY(velocity: Pair<Int, Int>, target: Pair<IntRange, IntRange>): Pair<Boolean, Int> {
    var x = 0; var y = 0
    var xVelocity = velocity.first; var yVelocity = velocity.second
    var maxY = y
    while (true) {
        x += xVelocity
        y += yVelocity

        if (y > maxY) maxY = y

        yVelocity--
        xVelocity--
        if (xVelocity < 0) {
            xVelocity = 0
        }

        // already missed the target
        if (x > target.first.last || y < target.second.first) return false to maxY

        if (x in target.first && y in target.second) return true to maxY
    }
}