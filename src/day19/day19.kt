package day19

import java.io.File

fun main() {
    val input = File("src/day19", "day19_input.txt").readLines()
    println("Stage 1 answer is ${stageOne(input)}") // 1681
    println("Stage 2 answer is ${stageTwo(input)}") // 5394
}

fun stageOne(input: List<String>): Int {
    return extract(input).sumOf { it.index * runSimulationForBlueprint(it, 24) }
}

fun stageTwo(input: List<String>): Int {
    return extract(input).take(3).map { runSimulationForBlueprint(it, 32) }.reduce(Int::times)
}

private fun runSimulationForBlueprint(blueprint: Blueprint, minutes: Int): Int {
    val deque = ArrayDeque<Simulation>()
    val cache = HashSet<Simulation>()
    deque.add(Simulation(minutes, 0, 0, 0, 0, 1, 0, 0, 0))

    var highscore = -1

    val best = HashMap<Int, Int>()

    while (deque.isNotEmpty()) {
        val state = deque.removeFirst()

        if (cache.contains(state)) {
            continue
        }
        cache.add(state)


        val maxGeodes = state.maxGeodes()
        if (best.getOrDefault(state.minute, -1) > maxGeodes) {
            continue
        }

        best[state.minute] = maxGeodes

        if (state.minute == 0) {
            if (state.geodes > highscore) {
                highscore = state.geodes
            }
        }

        if (state.ores >= blueprint.geodeRobotCostInOre && state.obsidian >= blueprint.geodeRobotCostInObsidian) {
            deque.add(state.buildGeodeRobot(blueprint))
        }

        if (state.oresRobots < blueprint.maxUsefulOreRobots) {
            if (state.ores >= blueprint.oreRobotCost) {
                deque.add(state.buildOreRobot(blueprint))
            }
        }


        if (state.clayRobots < blueprint.maxUsefulClayRobots) {
            if (state.ores >= blueprint.clayRobotCost) {
                deque.add(state.buildClayRobot(blueprint))
            }
        }

        if (state.obsidianRobots < blueprint.maxUsefulObsidianRobots) {
            if (state.ores >= blueprint.obsidianRobotCostInOre && state.clay >= blueprint.obsidianRobotCostInClay) {
                deque.add(state.buildObsidianRobot(blueprint))
            }
        }
        // just go without doing anything
        deque.add(state.onlyCollect(blueprint))
    }

    println("Finished ${blueprint.index} with $highscore geodes")
    return highscore
}


private fun extract(input: List<String>): List<Blueprint> {
    val regex =
        "Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.".toRegex()
    return input.map { regex.matchEntire(it)!!.groupValues.drop(1).map { it.toInt() } }.map { it.toBlueprint() }
}

private data class Blueprint(
    val index: Int,
    val oreRobotCost: Int,
    val clayRobotCost: Int,
    val obsidianRobotCostInOre: Int,
    val obsidianRobotCostInClay: Int,
    val geodeRobotCostInOre: Int,
    val geodeRobotCostInObsidian: Int,
    val maxUsefulOreRobots: Int = maxOf(oreRobotCost, clayRobotCost, obsidianRobotCostInOre, geodeRobotCostInOre),
    val maxUsefulClayRobots: Int = obsidianRobotCostInClay,
    val maxUsefulObsidianRobots: Int = geodeRobotCostInObsidian
)

private data class Simulation(
    var minute: Int,
    var ores: Int,
    var clay: Int,
    var obsidian: Int,
    var geodes: Int,
    var oresRobots: Int,
    var clayRobots: Int,
    var obsidianRobots: Int,
    var geodesRobots: Int
) {

    fun buildOreRobot(blueprint: Blueprint): Simulation {
        return this.copy(
            minute = minute - 1,
            ores = newOres(blueprint) - blueprint.oreRobotCost,
            clay = newClay(blueprint),
            obsidian = newObsidian(blueprint),
            geodes = geodes + geodesRobots,
            oresRobots = oresRobots + 1,
            clayRobots = clayRobots,
            obsidianRobots = obsidianRobots,
            geodesRobots = geodesRobots
        )
    }

    fun buildClayRobot(blueprint: Blueprint): Simulation {
        return this.copy(
            minute = minute - 1,
            ores = newOres(blueprint) - blueprint.clayRobotCost,
            clay = newClay(blueprint),
            obsidian = newObsidian(blueprint),
            geodes = geodes + geodesRobots,
            oresRobots = oresRobots,
            clayRobots = clayRobots + 1,
            obsidianRobots = obsidianRobots,
            geodesRobots = geodesRobots
        )
    }

    fun buildObsidianRobot(blueprint: Blueprint): Simulation {
        return this.copy(
            minute = minute - 1,
            ores = newOres(blueprint) - blueprint.obsidianRobotCostInOre,
            clay = newClay(blueprint) - blueprint.obsidianRobotCostInClay,
            obsidian = newObsidian(blueprint),
            geodes = geodes + geodesRobots,
            oresRobots = oresRobots,
            clayRobots = clayRobots,
            obsidianRobots = obsidianRobots + 1,
            geodesRobots = geodesRobots
        )
    }

    fun buildGeodeRobot(blueprint: Blueprint): Simulation {
        return this.copy(
            minute = minute - 1,
            ores = newOres(blueprint) - blueprint.geodeRobotCostInOre,
            clay = newClay(blueprint),
            obsidian = newObsidian(blueprint) - blueprint.geodeRobotCostInObsidian,
            geodes = geodes + geodesRobots,
            oresRobots = oresRobots,
            clayRobots = clayRobots,
            obsidianRobots = obsidianRobots,
            geodesRobots = geodesRobots + 1
        )
    }

    fun onlyCollect(blueprint: Blueprint): Simulation {
        return this.copy(
            minute = minute - 1,
            ores = newOres(blueprint),
            clay = newClay(blueprint),
            obsidian = newObsidian(blueprint),
            geodes = geodes + geodesRobots,
            oresRobots = oresRobots,
            clayRobots = clayRobots,
            obsidianRobots = obsidianRobots,
            geodesRobots = geodesRobots
        )
    }

    fun maxGeodes(): Int {
        val minutes = remainingMinutes()
        return (this.geodes + geodesRobots * minutes) + minutes * minutes - minutes
    }

    // after you stockpile max amount you can spend no need to check for those branches
    private fun newOres(blueprint: Blueprint): Int {
        val new = ores + oresRobots
        val maxPileThatCouldBeUsed = remainingMinutes() * blueprint.maxUsefulOreRobots
        return if (new > maxPileThatCouldBeUsed) maxPileThatCouldBeUsed else new
    }

    // after you stockpile max amount you can spend no need to check for those branches
    private fun newClay(blueprint: Blueprint): Int {
        val new = clay + clayRobots
        val maxPileThatCouldBeUsed = remainingMinutes() * blueprint.maxUsefulClayRobots
        return if (new > maxPileThatCouldBeUsed) {
            maxPileThatCouldBeUsed
        } else new
    }

    // after you stockpile max amount you can spend no need to check for those branches
    private fun newObsidian(blueprint: Blueprint): Int {
        val new = obsidian + obsidianRobots
        val maxPileThatCouldBeUsed = remainingMinutes() * blueprint.maxUsefulObsidianRobots
        return if (new > maxPileThatCouldBeUsed) maxPileThatCouldBeUsed else new
    }

    fun remainingMinutes() = this.minute
}

private fun List<Int>.toBlueprint(): Blueprint {
    return Blueprint(this[0], this[1], this[2], this[3], this[4], this[5], this[6])
}