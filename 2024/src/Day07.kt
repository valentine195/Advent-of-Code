typealias Calibration = Pair<Long, List<Long>>

fun main() {
    fun calc(acc: Long, index: Int, calibration: Calibration, concat: Boolean): Long {
        val (target, values) = calibration
        return when {
            acc == target && index >= values.size -> 1
            acc > target || index >= values.size -> 0
            else -> {
                calc(acc + values[index], index + 1, calibration, concat) +
                        calc(acc * values[index], index + 1, calibration, concat) +
                        if (concat) calc("$acc${values[index]}".toLong(), index + 1, calibration, true) else 0
            }
        }
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { it ->
            val (target, values) = it.split(Regex(""":?\s""")).let { parts ->
                parts[0].toLong() to parts.drop(1).map { it.toLong() }
            }

            if (calc(values[0], 0, target to values, false) > 0) target else 0
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { it ->
            val (target, values) = it.split(Regex(""":?\s""")).let { parts ->
                parts[0].toLong() to parts.drop(1).map { it.toLong() }
            }
            if (calc(values[0], 1, target to values, true) > 0) target else 0

        }
    }

// Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

// Read the input from the `src/Day01.txt` file.
    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}