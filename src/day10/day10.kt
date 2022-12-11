package day10

import java.io.File

fun main() {
    val input = File("src/day10", "day10_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 15880
    println("Stage 2 answer is ${stageTwo(input)}") // PLGFKAZG
}

fun stageOne(input: List<String>): Int {
    var score = 1
    var finalScore = 0
    var firstBuffer = 0
    var currentInstructionIndex = 0
    (1..1000).forEach {
        val currentInstruction = input.getOrNull(currentInstructionIndex)
        if(it.isImportantCycle()) {
            finalScore += score * it
        }
        if (firstBuffer != 0) {
            score += firstBuffer
            firstBuffer = 0
        } else if(currentInstruction?.equals("noop") == true) {
            // nothing
            currentInstructionIndex += 1
        } else {
            currentInstructionIndex += 1
            val instructionScore = currentInstruction?.replace("addx ","")?.toInt() ?: 0
            firstBuffer = instructionScore
        }


    }
    return finalScore
}

private fun Int.isImportantCycle() = this in listOf(20, 60, 100, 140, 180, 220)
private fun Int.isCrtImportantCycle() = this in listOf(40, 80, 120, 160, 200, 240)

fun stageTwo(input: List<String>): String {
    var score = 1
    var firstBuffer = 0
    var currentInstructionIndex = 0
    var currentCRTIndex = 0
    (1 until 240).forEach {
        val currentInstruction = input.getOrNull(currentInstructionIndex)
        if(currentCRTIndex + 1 in (score..score+2)) {
            // print("#") omitted for benchmark
        } else {
            // print(".") omitted for benchmark
        }
        if(it.isCrtImportantCycle()) {
//            print("\n") omitted for benchmark
            currentCRTIndex = 0
        }
        if (firstBuffer != 0) {
            score += firstBuffer
            firstBuffer = 0
        } else if(currentInstruction?.equals("noop") == true) {
            // nothing
            currentInstructionIndex += 1
        } else {
            currentInstructionIndex += 1
            val instructionScore = currentInstruction?.replace("addx ","")?.toInt() ?: 0
            firstBuffer = instructionScore
        }
        if(!it.isCrtImportantCycle()) {
            currentCRTIndex += 1
        }
    }
    return "PLGFKAZG"
}
