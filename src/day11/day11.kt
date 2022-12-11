package day11

import java.io.File

fun main() {
    val input = File("src/day11", "day11_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 55458
    println("Stage 2 answer is ${stageTwo(input)}") // 14508081294
}

fun stageOne(input: List<String>): Long {
    val monkeys = input.windowed(7, 7, true).map { it.parseToMonkey() }
    repeat(20) {
        monkeys.forEach {
            it.playRoundFirst(monkeys)
        }
    }

    return monkeys.map { it.inspections }.sorted().takeLast(2).reduce(Long::times)
}

fun stageTwo(input: List<String>): Long {
    val monkeys = input.windowed(7, 7, true).map { it.parseToMonkey() }
    // when checking if number is dividable by 3 and 5 you can add/subtract 3*5=15 to number and it won't change the output
    val divider = monkeys.map { it.test }.reduce(Int::times)
    repeat(10_000) {
        monkeys.forEach {
            it.playRoundSecond(monkeys, divider)
        }
    }

    return monkeys.map { it.inspections }.sorted().takeLast(2).reduce(Long::times)
}

private fun List<String>.parseToMonkey(): Monkey {
    val monkeyPattern = """Monkey (\d+):""".toRegex()
    val testPatter = """  Test: divisible by (\d+)""".toRegex()
    val ifTruePattern = """    If true: throw to monkey (\d+)""".toRegex()
    val ifFalsePattern = """    If false: throw to monkey (\d+)""".toRegex()

    val number = monkeyPattern.matchEntire(this[0])!!.groupValues[1].toInt()
    val startingItems = this[1].replace("  Starting items: ", "").split(", ").map { it.toLong() }.toMutableList()
    val operation = this[2].substring(23)
    val test = testPatter.matchEntire(this[3])!!.groupValues[1].toInt()
    val ifTrue = ifTruePattern.matchEntire(this[4])!!.groupValues[1].toInt()
    val ifFalse = ifFalsePattern.matchEntire(this[5])!!.groupValues[1].toInt()

    return Monkey(number, startingItems, operation, test, ifTrue, ifFalse)
}


private data class Monkey(
    val number: Int,
    val items: MutableList<Long>,
    val operation: String,
    val test: Int,
    val ifTrue: Int,
    val ifFalse: Int,
    var inspections: Long = 0
) {
    fun addToItems(item: Long) {
        items.add(item)
    }

    fun playRoundFirst(monkeys: List<Monkey>) {
        items.forEach {
            inspections++
            var worryLevel = applyOperation(it)
            worryLevel = worryLevel.floorDiv(3)

            if (worryLevel % test == 0L) {
                worryLevel.throwItem(monkeys[ifTrue])
            } else {
                worryLevel.throwItem(monkeys[ifFalse])
            }
        }
        items.clear()

    }

    fun playRoundSecond(monkeys: List<Monkey>, divider: Int) {
        items.forEach {
            inspections++
            var worryLevel = applyOperation(it)
            worryLevel %= divider

            if (worryLevel % test == 0L) {
                worryLevel.throwItem(monkeys[ifTrue])
            } else {
                worryLevel.throwItem(monkeys[ifFalse])
            }
        }
        items.clear()

    }

    private fun Long.throwItem(monkey: Monkey) = monkey.addToItems(this)

    private fun applyOperation(item: Long): Long {
        if (operation == "* old") return item * item
        if (operation[0] == '+') return item + operation.substring(2).toInt()
        if (operation[0] == '*') return item * operation.substring(2).toInt()
        throw Exception()
    }
}
