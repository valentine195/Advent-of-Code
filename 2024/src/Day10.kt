fun main() {

    fun delta(p1: Char, p2: Char): Int {
        return p2.toString().toInt() - p1.toString().toInt()
    }

    fun hike(start: Point, grid: Grid, visited: MutableSet<Point>): Int {
        if (grid[start] == '9') return 1

        val total = start.neighbors4().sumOf { neighbor ->
            when {
                visited.contains(neighbor) -> 0
                !grid.contains(neighbor) -> 0
                grid[neighbor] == '.' -> 0
                delta(grid[start]!!, grid[neighbor]!!) != 1 -> 0
                else -> hike(neighbor, grid, visited.also { it.add(neighbor) })
            }

        }
        return total
    }

    fun part1(input: List<String>): Int {
        return Grid(input).let {
            it.getByChar('0').sumOf { start ->
                hike(start, it, mutableSetOf(start))
            }
        }
    }

    fun distinct(start: Point, grid: Grid, visited: MutableSet<Point>): Int {
        if (grid[start] == '9') return 1

        return start.neighbors4().sumOf { neighbor ->
            when {
                visited.contains(neighbor) -> 0
                !grid.contains(neighbor) -> 0
                grid[neighbor] == '.' -> 0
                delta(grid[start]!!, grid[neighbor]!!) != 1 -> 0
                else -> distinct(neighbor, grid, visited.toMutableSet().also { it.add(neighbor) })
            }

        }
    }

    fun part2(input: List<String>): Int {
        return Grid(input).let {
            it.getByChar('0').sumOf { start ->
                distinct(start, it, mutableSetOf(start))
            }
        }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day10_test")
    check(part1(testInput).also { println("TEST: $it") } == 36)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day10")
    part1(input).println()

    check(part2(testInput).also { println("TEST: $it") } == 81)
    part2(input).println()
}