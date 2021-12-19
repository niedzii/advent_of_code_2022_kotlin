package day16

import java.io.File
import java.util.LinkedList
import kotlin.math.pow

fun main() {
    val input = File("src/day16", "day16_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 879
    println("Stage 2 answer is ${stageTwo(input)}") // 539051801941
}

fun stageOne(input: List<String>): Int {
    val inputAsBinary = input[0].hexToBinary()
    return parseForPartOne(inputAsBinary).first
}

private fun parseForPartOne(packet: String): Pair<Int, Int> {
    var version = packet.substringToDecimal(0..2).toInt()
    val typeId = packet.substringToDecimal(3..5)
    if (typeId == "4") { // literal value
        val literalValueAsBinary = calculateLiteralValue(packet)
        val bitsUsed = 6 + literalValueAsBinary.length + literalValueAsBinary.length / 4
        return version to bitsUsed
    } else { // operator
        val lengthTypeId = packet[6]
        val numberOfBits = if (lengthTypeId == '0') 15 else 11
        val lengthBits = packet.substringToDecimal(3 + 3 + 1..6 + numberOfBits).toInt()
        var checkedBits = 0
        if (lengthTypeId == '0') { // number of bits
            while (lengthBits != checkedBits) {
                val (subPacketVersion, length) = parseForPartOne(packet.substring(7 + numberOfBits + checkedBits))
                version += subPacketVersion
                checkedBits += length
            }
            val bitsUsed = checkedBits + numberOfBits + 7
            return version to bitsUsed
        } else { // number of packets
            for (i in 0 until lengthBits) {
                val (subPacketVersion, length) = parseForPartOne(packet.substring(7 + numberOfBits + checkedBits))
                version += subPacketVersion
                checkedBits += length
            }
            val bitsUsed = checkedBits + numberOfBits + 7
            return version to bitsUsed
        }
    }
}

data class Packet(var value: Long, var bitsUsed: Long, var isOperator: Boolean, val typeId: Long) {
    private val packets: MutableList<Packet> = mutableListOf()

    fun addPacket(operator: Packet) {
        this.packets.add(operator)
    }

    fun calculateValue(): Long {
        val queue = LinkedList<Packet>()
        queue.add(this)

        while (queue.isNotEmpty()) {
            val element = queue.pop()
            if (element.hasOnlyLiterals()) {
                element.reducePacket()
            } else {
                queue.addAll(element.packets.filter { it.isOperator })
                queue.add(element)
            }
        }
        return value
    }

    private fun reducePacket() {
        when (typeId) {
            0L -> (run { value = packets.sumOf { it.value } })
            1L -> (run { value = packets.map { it.value }.reduce { v1, v2 -> v1 * v2 } })
            2L -> (run { value = packets.minOf { it.value } })
            3L -> (run { value = packets.maxOf { it.value } })
            5L -> (run { value = if (packets[0].value > packets[1].value) 1 else 0 })
            6L -> (run { value = if (packets[0].value < packets[1].value) 1 else 0 })
            7L -> (run { value = if (packets[0].value == packets[1].value) 1 else 0 })
        }
        // is no longer and operator but a literal
        isOperator = false
    }

    private fun hasOnlyLiterals(): Boolean {
        return packets.all { !it.isOperator }
    }
}

private fun parseForPartTwo(packet: String): Packet {
    val typeId = packet.substringToDecimal(3..5).toInt()
    if (typeId == 4) { // literal value
        val literalValueAsBinary = calculateLiteralValue(packet)
        val packetValue = literalValueAsBinary.binaryToDecimal()
        val bitsUsed = 6 + literalValueAsBinary.length + literalValueAsBinary.length / 4
        return Packet(packetValue.toLong(), bitsUsed.toLong(), false, 0)
    } else { // operator

        val operatorVersion = packet.substringToDecimal(0..2).toInt()
        val operator = Packet(0, operatorVersion.toLong(), true, typeId.toLong())

        val lengthTypeId = packet[6]
        val numberOfBits = if (lengthTypeId == '0') 15 else 11
        val lengthBits = packet.substringToDecimal(3 + 3 + 1..6 + numberOfBits).toLong()
        var checkedBits = 0L
        if (lengthTypeId == '0') { // number of bits
            while (lengthBits != checkedBits) {
                val subOperator = parseForPartTwo(packet.substring(7 + numberOfBits + checkedBits.toInt()))
                checkedBits += subOperator.bitsUsed
                operator.addPacket(subOperator)
            }
            val bitsUsed = checkedBits + numberOfBits + 7L
            operator.bitsUsed = bitsUsed
            return operator
        } else { // number of packets
            for (i in 0 until lengthBits) {
                val subOperator = parseForPartTwo(packet.substring(7 + numberOfBits + checkedBits.toInt()))
                checkedBits += subOperator.bitsUsed
                operator.addPacket(subOperator)
            }
            val bitsUsed = checkedBits + numberOfBits + 7
            operator.bitsUsed = bitsUsed
            return operator
        }
    }
}

private fun calculateLiteralValue(packet: String): String {
    var numbers = ""
    var index = 0
    do {
        val from = index * 5 + 7
        val to = index * 5 + 10
        numbers += (packet.substring(from..to))
        val firstBit = packet[6 + index * 5]
        index++
    } while (firstBit == '1')
    return numbers
}

fun stageTwo(input: List<String>): Long {
    val inputAsBinary = input[0].hexToBinary()
    return parseForPartTwo(inputAsBinary).calculateValue()
}

private fun String.hexToBinary(): String {
    return this.map(::hexCharToBinary).joinToString(separator = "")
}

private fun hexCharToBinary(char: Char): String {
    return when (char) {
        '0' -> "0000"
        '1' -> "0001"
        '2' -> "0010"
        '3' -> "0011"
        '4' -> "0100"
        '5' -> "0101"
        '6' -> "0110"
        '7' -> "0111"
        '8' -> "1000"
        '9' -> "1001"
        'A' -> "1010"
        'B' -> "1011"
        'C' -> "1100"
        'D' -> "1101"
        'E' -> "1110"
        'F' -> "1111"
        else -> ""
    }
}

private fun String.substringToDecimal(range: IntRange): String {
    return this.substring(range).binaryToDecimal()
}

private fun String.binaryToDecimal(): String {
    val reversed = this.reversed()
    var result = 0L

    reversed.forEachIndexed { index, c ->
        result += (2.0.pow(index) * c.digitToInt()).toLong()
    }
    return result.toString()
}