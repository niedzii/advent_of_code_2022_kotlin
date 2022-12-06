import day1.main as day1
import day2.main as day2
import day3.main as day3
import day4.main as day4
import day5.main as day5
import day6.main as day6

fun main() {
    val startTime = System.currentTimeMillis();
    day1()
    day2()
    day3()
    day4()
    day5()
    day6()
    val endTime = System.currentTimeMillis()
    println("Time taken : ${endTime - startTime} ms")
}