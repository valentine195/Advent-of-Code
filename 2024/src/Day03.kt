fun main() {
    fun part1(input: List<String>): Long {
        val rex = """mul\((?<left>\d+?),(?<right>\d+?)\)""".toRegex()

        return input.sumOf { line ->
            rex.findAll(line)
                .fold(0.toLong()) { a, b -> a + b.groups["left"]!!.value.toLong() * b.groups["right"]!!.value.toLong() }
        }

    }

    fun part2(input: List<String>): Long {
        val rex = """((?<mul>mul\((?<left>\d+),(?<right>\d+)\))|(?<disable>don't\(\))|(?<enable>do\(\)))""".toRegex()

        var enabled = true
        return input.sumOf { line ->
            rex.findAll(line)
                .fold(0.toLong()) { a, b ->
                    a + when (b.value) {
                        "don't()" -> {
                            enabled = false
                            0
                        }

                        "do()" -> {
                            enabled = true
                            0
                        }

                        else -> if (enabled) b.groups["left"]!!.value.toLong() * b.groups["right"]!!.value.toLong() else 0

                    }.toLong()
                }
        }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput1 = listOf("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")
    check(part1(testInput1) == 161.toLong())
    val testInput2 = listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")
    check(part2(testInput2) == 48.toLong())
    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}