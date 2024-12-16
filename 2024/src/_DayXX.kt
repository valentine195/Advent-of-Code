fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    //Part 1
    val testInput = readInput("DayXX_test")
    check(part1(testInput) == 1)
    val input = readInput("DayXX")
    part1(input).println()

    //Part 2
    check(part2(testInput) == 1)
    part2(input).println()
}