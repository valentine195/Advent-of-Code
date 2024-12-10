fun main() {
    fun <T> uniquePairs(list: List<T>): List<Pair<T, T>> {
        return list.indices.flatMap { i ->
            (i + 1 until list.size).map { j -> list[i] to list[j] }
        }
    }

    fun part1(input: List<String>): Int {
        val (width, height) = input[0].length to input.size

        val antennae = input.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, c -> if (c != '.') c to Point(x, y) else null }
        }.groupBy({ it.first }, { it.second })
            .mapValues { (_, points) -> uniquePairs(points) }

        val uniques = antennae.values
            .flatMap { pairs ->
                pairs.flatMap { (p1, p2) ->
                    val distance = p1.distance(p2)
                    listOf(p2.add(distance), p1.subtract(distance))
                }
            }
            .filter { it.x in 0 until width && it.y in 0 until height }.toSet()
        return uniques.size
    }

    fun part2(input: List<String>): Int {
        val (width, height) = input[0].length to input.size
        val inside = { p: Point -> p.x in 0 until width && p.y in 0 until height }
        val antennae = input.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, c -> if (c != '.') c to Point(x, y) else null }
        }.groupBy({ it.first }, { it.second })
            .mapValues { (_, points) -> uniquePairs(points) }

        val uniques = antennae.values
            .flatMap { pairs ->
                pairs.flatMap { (p1, p2) ->
                    val distance = p1.distance(p2)

                    generateSequence(p2.add(distance)) { it.add(distance) }
                        .takeWhile { inside(it) }
                        .toList() +
                            generateSequence(p1.subtract(distance)) { it.subtract(distance) }
                                .takeWhile { inside(it) }
                                .toList() +
                            listOf(p1, p2)
                }
            }
            .filter { inside(it) }.toSet()

        return uniques.size
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}