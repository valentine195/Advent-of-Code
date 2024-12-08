fun main() {
    data class Point(val x: Int, val y: Int)

    fun distance(p1: Point, p2: Point): Point {
        return Point(p2.x - p1.x, p2.y - p1.y)
    }

    fun add(point: Point, distance: Point): Point {
        return Point(point.x + distance.x, point.y + distance.y)
    }

    fun subtract(point: Point, distance: Point): Point {
        return Point(point.x - distance.x, point.y - distance.y)
    }

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
                    val distance = distance(p1, p2)
                    listOf(add(p2, distance), subtract(p1, distance))
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
                    val distance = distance(p1, p2)

                    generateSequence(add(p2, distance)) { add(it, distance) }
                        .takeWhile { inside(it) }
                        .toList() +
                            generateSequence(subtract(p1, distance)) { subtract(it, distance) }
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