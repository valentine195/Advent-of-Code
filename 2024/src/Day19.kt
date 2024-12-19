fun main() {

    fun combos(towels: Set<String>, towel: String, cache: MutableMap<String, Long>): Long {
        return cache.getOrPut(towel) {
            var sum = 0L
            for (t in towels) {
                if (towel.startsWith(t)) {
                    sum += combos(towels, towel.removePrefix(t), cache)
                }
            }
            sum
        }
    }


    fun part1(input: List<String>): Int {
        val towels = input.first().split(", ").toMutableSet()
        val designs = input.takeLastWhile { it.isNotEmpty() }

        return designs.count { design ->
            val cache: MutableMap<String, Long> = mutableMapOf("" to 1L)
            combos(towels, design, cache) > 0
        }
    }

    fun part2(input: List<String>): Long {
        val towels = input.first().split(", ").toMutableSet()
        val designs = input.takeLastWhile { it.isNotEmpty() }

        return designs.sumOf { design ->
            val cache: MutableMap<String, Long> = mutableMapOf("" to 1L)
            combos(towels, design, cache)
        }
    }

//Part 1
    val testInput = readInput("Day19_test")
    check(part1(testInput) == 6)
    val input = readInput("Day19")
    part1(input).println()

//Part 2
    check(part2(testInput) == 16L)
    part2(input).println()
}