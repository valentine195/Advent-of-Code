fun main() {
    fun isOrdered(input: List<Int>, instructions: Map<Int, List<Int>>): Boolean {
        return input.indices.all { index ->
            if (index >= input.size) {
                true
            } else {
                !input.slice(index + 1..<input.size)
                    .any { instructions.getOrDefault(it, listOf()).contains(input[index]) }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val instructions = input.takeWhile { it.trim().isNotEmpty() }.map { it.split("|").map { i -> i.toInt() } }
            .groupBy({ it[0] }, { it[1] })
        val prints = input.takeLastWhile { it.trim().isNotEmpty() }.map { it.split(",").map { i -> i.toInt() } }

        return prints.filter { isOrdered(it, instructions) }.sumOf { it[it.size / 2] }
    }

    fun part2(input: List<String>): Int {
        val instructions = input.takeWhile { it.trim().isNotEmpty() }.map { it.split("|").map { i -> i.toInt() } }
            .groupBy({ it[0] }, { it[1] })
        val prints = input.takeLastWhile { it.trim().isNotEmpty() }.map { it.split(",").map { i -> i.toInt() } }


        return prints.filter { !isOrdered(it, instructions) }
            .map {
                it.sortedWith { a, b ->
                    if (isOrdered(listOf(a, b), instructions)) {
                        -1
                    } else {
                        1
                    }
                }
            }.sumOf { it[it.size / 2] }

    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}