import java.util.PriorityQueue

typealias DirectedPoint = Pair<Point, Direction>

fun main() {
    fun print(grid: Map<DirectedPoint, Char>, bounds: Point, path: List<DirectedPoint>) {
        val mapped = grid.mapKeys { it.key.first }
        val str = mutableListOf<String>()

        str.add(" " + (0 until bounds.x).joinToString("") { ('a' + (it)).toString() })
        for (y in 0 until bounds.y) {
            val line = mutableListOf<Char>(('a' + (y)))
            for (x in 0 until bounds.x) {
                val pathPoint = path.find { (point) -> point == Point(x, y) }
                val char = pathPoint?.second?.char() ?: mapped.getOrDefault(Point(x, y), '#')
                line.add(
                    char
                )
            }
            str.add(line.joinToString(""))
        }
        (str.joinToString("\n") + "\n").println()
    }

    fun cost(start: DirectedPoint, next: DirectedPoint): Int {
        val point = if (start.first == next.first) 0 else 1
        return when (start.second) {
            next.second -> point
            next.second.cw(), next.second.ccw() -> 1000 + point
            else -> 2000 + point
        }
    }

    fun traverse(
        start: Point, finish: Point, grid: Map<DirectedPoint, Char>, bounds: Point
    ): Int {
        val directedStart = DirectedPoint(start, Direction.RIGHT)
        val frontier =
            PriorityQueue<Pair<DirectedPoint, Int>> { a, b -> (a.second - b.second) }.also { it.add(directedStart to 0) }
        val cameFrom = hashMapOf<DirectedPoint, DirectedPoint>()
        val costs = hashMapOf<Point, Int>(start to 0)

        while (frontier.isNotEmpty()) {
            val current = frontier.poll().first
            if (current.first == finish) {
                val path = mutableListOf<DirectedPoint>()
                var pathing = current
                while (pathing.first != start) {
                    path.add(pathing)
                    pathing = cameFrom[pathing]!!
                }
                path.reverse() // optional
                print(grid, bounds, path)
                return costs[current.first]!!
            }

            /**
             * At this point, you need to either TURN CW, TURN CCW, or MOVE ONE STRAIGHT
             */
            val points = listOf(
                current.first + current.second to current.second,
                current.first + current.second.cw() to current.second.cw(),
                current.first + current.second.ccw() to current.second.ccw()
            )
            for (next in points.filter { it in grid }) {
                val cost = costs[current.first]!! + cost(current, next)
                if (next.first !in costs || cost < costs[next.first]!!) {
                    costs[next.first] = cost
                    frontier.add(next to cost)
                    cameFrom[next] = current
                }
            }


        }
        throw IllegalArgumentException()
    }

    fun part1(input: List<String>): Int {
        lateinit var reindeer: Point
        lateinit var end: Point
        val grid: Map<DirectedPoint, Char> = input.foldIndexed(hashMapOf<DirectedPoint, Char>()) { y, grid, row ->
            row.toCharArray().forEachIndexed { x, c ->
                if (c == '#') return@forEachIndexed
                if (c == 'S') reindeer = Point(x, y)
                if (c == 'E') end = Point(x, y)
                Direction.entries.forEach {
                    grid[Point(x, y) to it] = c
                }
            }
            grid
        }


        return traverse(reindeer, end, grid, Point(input[0].length, input.size))
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    //Part 1
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 7036)
    val input = readInput("Day16")
    part1(input).println()

    //Part 2
    check(part2(testInput) == 1)
    part2(input).println()
}