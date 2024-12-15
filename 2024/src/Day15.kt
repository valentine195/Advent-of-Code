fun main() {
    fun part1(input: List<String>): Int {
        fun shouldMove(grid: Grid, point: Point, dir: Direction): List<Point> {
            val list = generateSequence(point) { it + dir }.takeWhile {
                grid[it] == 'O' || grid[it] == '@'
            }.toList()
            return if (grid[list.last() + dir] == '.') list else listOf()
        }

        val grid = Grid(input.takeWhile { it.isNotEmpty() })
        val moves = input.takeLastWhile { it.isNotEmpty() }.map {
            it.map { c ->
                when (c) {
                    '^' -> Direction.UP
                    '>' -> Direction.RIGHT
                    '<' -> Direction.LEFT
                    else -> Direction.DOWN
                }
            }
        }.flatten()

        var robot = grid.getByChar('@')[0]

        for (dir in moves) {
            /*val dir = moves[i]*/
            val moving = shouldMove(grid, robot, dir)
            val cached = moving.associateWith { grid[it] }
            // Clear the old positions
            moving.forEach { point ->
                grid[point] = '.' // Mark the old position as empty
            }

            // Update the new positions
            moving.forEach { point ->
                val newPoint = point + dir
                grid[newPoint] = cached[point]!! // Mark the new position
                if (grid[newPoint] == '@') robot = newPoint
            }
        }

        return grid.findAll('O').sumOf { (it.x) + (it.y) * 100 }
    }

    fun part2(input: List<String>): Int {
        fun shouldMove(grid: Grid, robot: Point, dir: Direction): List<Point> {
            val top = mutableListOf<Point>(robot)
            val considered = mutableListOf<Point>(robot)
            while (true) {
                val next = top.map { it + dir }.sorted().toMutableList()
                if (dir == Direction.UP) {
                    if (grid[next.first()] == ']') next.add(0, next.first() + dir.ccw())
                    if (grid[next.last()] == '[') next.add(next.last() + dir.cw())
                }
                if (dir == Direction.DOWN) {
                    if (grid[next.first()] == ']') next.add(0, next.first() + dir.cw())
                    if (grid[next.last()] == '[') next.add(next.last() + dir.ccw())
                }
                if (next.any { grid[it] == '#' }) return listOf()
                if (next.all { grid[it] == '.' }) return considered.toList()
                considered.addAll(next.filter { grid[it] != '.' })
                top.clear()
                top.addAll(next.filter { grid[it] != '.' })
            }
        }

        val grid = Grid(input.takeWhile { it.isNotEmpty() }.map {
            it.replace(".", "..").replace("#", "##").replace("O", "[]").replace("@", "@.")
        })
        val moves = input.takeLastWhile { it.isNotEmpty() }.map {
            it.map { c ->
                when (c) {
                    '^' -> Direction.UP
                    '>' -> Direction.RIGHT
                    '<' -> Direction.LEFT
                    else -> Direction.DOWN
                }
            }
        }.flatten()

        var robot = grid.getByChar('@')[0]
        for (dir in moves/*.subList(0, 8)*/) {
            /*val dir = moves[i]*/
            val moving = shouldMove(grid, robot, dir)
            val cached = moving.associateWith { grid[it] }
            // Clear the old positions
            moving.forEach { point ->
                grid[point] = '.' // Mark the old position as empty
            }

            // Update the new positions
            moving.forEach { point ->
                val newPoint = point + dir
                grid[newPoint] = cached[point]!! // Mark the new position
                if (grid[newPoint] == '@') robot = newPoint
            }
        }

        return grid.findAll('[').sumOf { (it.x) + (it.y) * 100 }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 10092)
    check(part2(testInput) == 9021)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}