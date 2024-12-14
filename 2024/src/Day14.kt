fun main() {
    data class Robot(var point: Point, val velocity: Point, val maxX: Int, val maxY: Int) {
        fun wrapAround(v: Int, delta: Int, max: Int): Int {
            return if (delta >= 0) {
                (v + delta) % max
            } else {
                ((v + delta) - delta * max) % max
            }
        }

        fun move(seconds: Int): Point {
            return Point(
                wrapAround(point.x, velocity.x * seconds, maxX) % maxX, wrapAround(point.y, velocity.y * seconds, maxY)
            )
        }

        fun quadrant(): Int {
            return when {
                point.x < (maxX - 1) / 2 && point.y < (maxY - 1) / 2 -> 0
                point.x > (maxX - 1) / 2 && point.y < (maxY - 1) / 2 -> 1
                point.x < (maxX - 1) / 2 && point.y > (maxY - 1) / 2 -> 2
                else /*point.x > (maxX - 1) / 2 && point.y > (maxY - 1) / 2*/ -> 3

            }


        }
    }

    fun part1(input: List<String>, maxX: Int, maxY: Int): Int {
        return input.map { line ->
            line.split(" ").map { s -> s.split("=").last().split(",").map { v -> v.toInt() } }
                .let { Robot(Point(it[0][0], it[0][1]), Point(it[1][0], it[1][1]), maxX, maxY) }.also {
                    it.point = it.move(100)
                }
        }.filter { it.point.x != (maxX - 1) / 2 && it.point.y != (maxY - 1) / 2 }
            .groupBy { it.quadrant() }.values.fold(1) { acc, it -> acc * it.size }.also { it.println() }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

// Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day14_test")
    check(part1(testInput, 11, 7) == 12)
    /*check(part2(testInput) == 1)*/

// Read the input from the `src/Day01.txt` file.
    val input = readInput("Day14")
    part1(input, 101, 103).println()
    part2(input).println()
}