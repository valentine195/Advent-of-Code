typealias Looping = Int

enum class Direction() {
    UP, RIGHT, DOWN, LEFT;

    /*fun getChar(): Char {
        return this.char
    }*/

    fun move(): Point {
        return when (this) {
            UP -> Point(0, -1)
            RIGHT -> Point(1, 0)
            DOWN -> Point(0, 1)
            LEFT -> Point(-1, 0)
        }
    }

    fun rotate(): Direction {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }
}

fun main() {

    /**
     * Returns a tuple of ( visited points, Looping bit )
     * Looping == 1 => Entered a loop
     */
    fun walk(grid: Grid): Pair<Set<Point>, Looping> {
        var direction = Direction.UP
        var guard = grid.find('^')!!
        val visited: HashSet<Pair<Point, Direction>> = hashSetOf()
        while (visited.add(Pair(guard, direction)) /* returns false if present */) {
            val next = guard.add(direction.move())
            when (grid[next]) {
                '#' -> direction = direction.rotate()
                is Char -> guard = next
                /** no return -> found the exit */
                else -> return Pair(visited.map { it.first }.toSet(), 0)
            }
        }

        return Pair(visited.map { it.first }.toSet(), 1)
    }

    fun part1(input: List<String>): Int {
        return walk(Grid(input)).first.size
    }

    fun part2(input: List<String>): Int {

        return walk(Grid(input)).first.sumOf { point ->
            /** For each walked point (except the first), replace it with a wall and walk it */
            Grid(input).apply {
                if (this[point] != '^') this[point] = '#'
            }.let {
                walk(it).second
            }
        }
    }

// Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)
// Read the input from the `src/Day01.txt` file.
    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}