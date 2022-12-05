package day5

import java.io.File

fun main() {
    val input = File("src/day5", "day5_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // VRWBSFZWM
    println("Stage 2 answer is ${stageTwo(input)}") // RBTWJWMCF
}

fun stageOne(input: List<String>): String {
    val (lists, instructions) = parseInput(input)

    instructions.forEach {
        parseInstructionOldCrane(lists, it)
    }

    return lists.map { it.last() }.joinToString(separator = "")
}

fun stageTwo(input: List<String>): String {
    val (lists, instructions) = parseInput(input)

    instructions.forEach {
        parseInstructionNewCrane(lists, it)
    }

    return lists.map { it.last() }.joinToString(separator = "")
}

private fun parseInstructionOldCrane(lists: MutableList<MutableList<Char>>, instruction: Instruction) {
    (0 until instruction.count).forEach { _ ->
        moveAsOldCrane(lists[instruction.from - 1], lists[instruction.to - 1])
    }
}

private fun parseInstructionNewCrane(lists: MutableList<MutableList<Char>>, instruction: Instruction) {
    moveAsNewCrane(lists[instruction.from - 1], lists[instruction.to - 1], instruction.count)
}

private fun moveAsNewCrane(from: MutableList<Char>, to: MutableList<Char>, count: Int) {
    to.addAll(from.takeLast(count))
    (0 until count).forEach { _ ->
        from.removeLast()
    }
}

private fun moveAsOldCrane(from: MutableList<Char>, to: MutableList<Char>) {
    if(from.size == 0) return
    to.add(from.last())
    from.removeLast()
}

private fun parseInput(input: List<String>): Pair<MutableList<MutableList<Char>>, List<Instruction>> {
    val lists = mutableListOf<MutableList<Char>>()
    val size = input.find { it.startsWith(" 1 ") }!!.replace(" ", "").toCharArray().last().digitToInt()
    (0 until size).forEach { _ ->
        lists.add(mutableListOf())
    }

    input
        .filter { it.contains("[") }
        .forEach {
        it.windowed(4, 4, true)
            .forEachIndexed { index, ele ->
                if (ele[1] != ' ') {
                    lists[index].add(ele[1])
                }
            }
    }

    lists.forEach { it.reverse() }

    val instructions = input
        .asSequence()
        .filter { it.startsWith("move") }
        .map { it.replace("move ", "") }
        .map { it.replace(" from ", " ") }
        .map { it.replace(" to ", " ") }
        .map { it.split(" ") }
        .map { it.toInstruction() }
        .toList()

    return lists to instructions
}

private fun List<String>.toInstruction() = Instruction(this[0].toInt(), this[1].toInt(), this[2].toInt())

private data class Instruction(val count: Int, val from: Int, val to: Int)
