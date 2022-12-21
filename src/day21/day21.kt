package day21

import java.io.File
import java.lang.RuntimeException

fun main() {
    val input = File("src/day21", "day21_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 56490240862410
    println("Stage 2 answer is ${stageTwo(input)}") // 3403989691757
}

fun stageOne(input: List<String>): Long {
    val map = HashMap<String, String>(input.size)
    input.map { it.splitToSequence(": ").take(2) }.forEach { map.put(it.first(), it.last()) }

    return calculate("root", map, false)
}

fun stageTwo(input: List<String>): Long {
    val map = HashMap<String, String>(input.size)
    input.map { it.splitToSequence(": ").take(2) }.forEach { map.put(it.first(), it.last()) }

    val whatToMatch = mutableMapOf<String, Long>()

    var searched = "root"
    while (true) {
        val calculateMatch = calculateMatch(searched, map, whatToMatch)
        if (calculateMatch.second != -1L) {
            return calculateMatch.second
        }
        searched = calculateMatch.first
    }
}

private fun calculateMatch(
    monkey: String,
    map: Map<String, String>,
    whatToMatch: MutableMap<String, Long>
): Pair<String, Long> {
    var withoutHumanIn: Long? = 0
    var withHumanIn: String? = null

    val nowMonkey = map[monkey]!!

    if (nowMonkey.contains("humn")) {
        val amountToMatch = whatToMatch[monkey]!!
        val secondMonkey = nowMonkey.substringAfterLast(" ")

        // human should yell
        val second = calculate(secondMonkey, map, true)

        val human = amountToMatch.plus(second)
        return "notImportant" to human
    }


    val firstMonkey = nowMonkey.substringBefore(" ")
    val secondMonkey = nowMonkey.substringAfterLast(" ")


    try {
        val first = calculate(firstMonkey, map, true)
        withoutHumanIn = first
    } catch (e: HumanException) {
        withHumanIn = firstMonkey
    }

    try {
        val second = calculate(secondMonkey, map, true)
        withoutHumanIn = second
    } catch (e: HumanException) {
        withHumanIn = secondMonkey
    }

    // only root checks for equality
    if (monkey == "root") {
        whatToMatch[withHumanIn!!] = withoutHumanIn!!
    } else {
        // calculate with reversed math what value should part with human in it eventually have
        val amountToMatch = whatToMatch[monkey]!!

        if (firstMonkey == withHumanIn) {
            val putTuMap = when (nowMonkey.split(" ")[1]) {
                "+" -> amountToMatch - withoutHumanIn!!
                "-" -> amountToMatch + withoutHumanIn!!
                "*" -> amountToMatch / withoutHumanIn!!
                "/" -> amountToMatch * withoutHumanIn!!
                else -> error("F")
            }
            whatToMatch[withHumanIn] = putTuMap

        } else {
            val putTuMap = when (nowMonkey.split(" ")[1]) {
                "+" -> amountToMatch - withoutHumanIn!!
                "-" -> withoutHumanIn!! - amountToMatch
                "*" -> amountToMatch / withoutHumanIn!!
                "/" -> withoutHumanIn!! / amountToMatch
                else -> error("F")
            }
            whatToMatch[withHumanIn!!] = putTuMap

        }
    }

    return withHumanIn to -1L
}

private fun calculate(monkey: String, map: Map<String, String>, shouldThrow: Boolean): Long {
    val shout = map[monkey] ?: error(monkey)

    if (shouldThrow && shout.contains("humn")) throw HumanException()

    shout.toLongOrNull()?.let { return it }

    val split = shout.split(" ")

    val first = calculate(split[0], map, shouldThrow)
    val second = calculate(split[2], map, shouldThrow)

    return when (split[1]) {
        "+" -> first + second
        "-" -> first - second
        "*" -> first * second
        "/" -> first / second
        else -> error(split[1])
    }
}

private class HumanException : RuntimeException()