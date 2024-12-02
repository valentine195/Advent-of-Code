import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {

        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()

        input.forEach {
            val split = it.split("\\s+".toRegex())
            left.add(split[0].toInt())
            right.add(split[1].toInt())
        }
        left.sort()
        right.sort()

        val distances = left.mapIndexed { index, it ->
            abs(it - right[index])
        }
        return distances.sum()
    }

    fun part2(input: List<String>): Int {
        val left = mutableListOf<Int>()
        val right = mutableMapOf<Int, Int>()

        input.forEach {
            val split = it.split("\\s+".toRegex())
            left.add(split[0].toInt())
            right[split[1].toInt()] = right.getOrDefault(split[1].toInt(), 0) + 1
        }

        var similarity = 0
        for (item in left) {
            similarity +=  item * right.getOrDefault(item, 0)
        }

        return similarity
    }

    // Test if implementation meets criteria from the description, like:
    /*     check(part1(listOf("test_input")) == 1) */

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
