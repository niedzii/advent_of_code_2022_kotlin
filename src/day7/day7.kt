package day7

import java.io.File

fun main() {
    val input = File("src/day7", "day7_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 1232307
    println("Stage 2 answer is ${stageTwo(input)}") // 7268994
}

fun stageOne(input: List<String>): Int {
    val root = createFileSystem(input)
    return root.traversePartOne().sumOf { it.calculateSize() }
}

fun stageTwo(input: List<String>): Int {
    val root = createFileSystem(input)

    val totalSystemSpace = 70_000_000
    val goal = 30_000_000
    val occupiedSpace = root.calculateSize()
    val currentSpace = totalSystemSpace - occupiedSpace

    val needed = goal - currentSpace
    return root.traversePartTwo().map { it.calculateSize() }.sortedByDescending { it }.takeWhile { it > needed }.last()
}

private fun createFileSystem(input: List<String>): Directory {
    val root = Directory("/")
    var currentNode: Directory = root

    input.forEach {
        if (it.startsWith("$")) {
            // command
            if (it.substring(2, 4) == "cd") {
                val destination = it.substring(5)

                currentNode = when (destination) {
                    "/" -> {
                        root
                    }
                    ".." -> {
                        currentNode.parent!!
                    }
                    else -> {
                        currentNode.childrens.find { it.name == destination } as Directory
                    }
                }
            }
            if (it.substring(2, 4) == "ls") {
                // nothing
            }
        } else {
            // ls output
            if (it.startsWith("dir")) {
                // is directory
                val name = it.substring(4)
                currentNode.addChildren(Directory(name, currentNode))
            } else {
                // file
                val split = it.split(" ")
                currentNode.addChildren(File(split[1], split[0].toInt()))
            }


        }

    }
    return root
}


abstract class Node(val name: String, val size: Int, val parent: Directory? = null, val childrens: MutableList<Node> = mutableListOf()) {
    abstract fun calculateSize() : Int
}

class Directory(name: String, parent: Directory? = null) : Node(name, 0, parent) {

    fun addChildren(child: Node) = childrens.add(child)

    override fun calculateSize() = childrens.sumOf { it.calculateSize() }

    fun traversePartOne(): List<Directory> {
        val ofChildren = childrens.filterIsInstance<Directory>().flatMap { it.traversePartOne() }.toMutableList()

        if(this.calculateSize() < 100_000) ofChildren.add(this)

        return ofChildren

    }

    fun traversePartTwo(): List<Directory> {
        val ofChildren = childrens.filterIsInstance<Directory>().flatMap { it.traversePartTwo() }.toMutableList()
        ofChildren.add(this)
        return ofChildren
    }
}

class File(name: String, size: Int) : Node(name, size) {
    override fun calculateSize() = size
}