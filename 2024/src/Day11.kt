fun main() {

    fun blink(stones: Map<Long, Long>): Map<Long, Long> {
        return stones.entries
            .flatMap { (stone, count) ->
                when {
                    stone == 0L -> listOf(1L to count)
                    stone.toString().length % 2 != 0 -> listOf(stone * 2024 to count)
                    else -> stone.toString().chunked(stone.toString().length / 2) { it.toString().toLong() }
                        .map { it to count }
                }
            }.groupBy { it.first }.mapValues { (_, values) -> values.sumOf { it.second } }
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            val stones = line.split(' ').associate { it.toLong() to 1L }
            generateSequence(blink(stones)) { blink(it) }.take(25).last().values.sum()
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { line ->
            val stones = line.split(' ').associate { it.toLong() to 1L }
            generateSequence(blink(stones)) { blink(it) }.take(75).last().values.sum()
        }
    }

// Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 55312L)

// Read the input from the `src/Day01.txt` file.
    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}