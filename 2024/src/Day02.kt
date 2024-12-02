import kotlin.math.abs

fun main() {
    fun isSorted(input: List<Pair<Int, Int>>): Boolean {
        return input.all { (a, b) -> a < b } || input.all { (a, b) -> a > b }
    }

    fun isWithinRange(input: List<Pair<Int, Int>>): Boolean {
        return input.all { (a, b) -> abs(a - b) <= 3 }
    }

    fun isSafe(input: List<Int>): Boolean {
        val zipped = input.zipWithNext();
        return isSorted(zipped) && isWithinRange(zipped)
    }


    fun part1(input: List<String>): Int = input.count { line -> isSafe(line.split(' ').map { it.toInt() }) }


    fun part2(input: List<String>): Int {
        val lines = input.map { line -> line.split(' ').map { it.toInt() } }

        return lines.count {
            isSafe(it) || it.indices.any { i ->
                isSafe(it.toMutableList().apply { removeAt(i) })
            }
        }
    }
    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}