import day1.main as day1
import day2.main as day2

fun main() {
    val startTime = System.currentTimeMillis();
    day1()
    day2()
    val endTime = System.currentTimeMillis()
    println("Time taken : ${endTime - startTime} ms")
}