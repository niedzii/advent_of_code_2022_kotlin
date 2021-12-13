import day1.main as day1
import day2.main as day2
import day3.main as day3
import day4.main as day4
import day5.main as day5
import day6.main as day6
import day7.main as day7
import day8.main as day8
import day9.main as day9
import day10.main as day10
import day11.main as day11
import day12.main as day12
import day13.main as day13

fun main() {
    val startTime = System.currentTimeMillis()
    day1()
    day2()
    day3()
    day4()
    day5()
    day6()
    day7()
    day8()
    day9()
    day10()
    day11()
    day12()
    day13()
    val endTime = System.currentTimeMillis()
    println("Time taken : ${endTime - startTime} ms")
}