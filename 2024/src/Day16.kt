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
        start: DirectedPoint, finish: Point, grid: Map<Point, Char>
    ): Int {
        val frontier =
            PriorityQueue<Pair<DirectedPoint, Int>> { a, b -> (a.second - b.second) }.also { it.add(start to 0) }
        val cameFrom = hashMapOf<DirectedPoint, DirectedPoint>()
        val costs = hashMapOf<Point, Int>(start.first to 0)

        while (frontier.isNotEmpty()) {
            val current = frontier.poll().first
            if (current.first == finish) {
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
            for (next in points.filter { it.first in grid }) {
                val cost = costs[current.first]!! + cost(current, next)
                if (next.first !in costs || cost < costs[next.first]!!) {
                    costs[next.first] = cost
                    frontier.add(next to cost)
                    cameFrom[next] = current
                }
            }


        }
        return Int.MAX_VALUE
    }

    fun part1(input: List<String>): Int {
        lateinit var reindeer: Point
        lateinit var end: Point
        val grid: Map<Point, Char> = input.foldIndexed(hashMapOf<Point, Char>()) { y, grid, row ->
            row.toCharArray().forEachIndexed { x, c ->
                if (c == '#') return@forEachIndexed
                if (c == 'S') reindeer = Point(x, y)
                if (c == 'E') end = Point(x, y)
                grid[Point(x, y)] = c
            }
            grid
        }


        return traverse(reindeer to Direction.RIGHT, end, grid)
    }

    fun part2(input: List<String>, target: Int): Int {
        lateinit var reindeer: Point
        lateinit var end: Point
        val grid: Map<Point, Char> = input.foldIndexed(hashMapOf<Point, Char>()) { y, grid, row ->
            row.toCharArray().forEachIndexed { x, c ->
                if (c == '#') return@forEachIndexed
                if (c == 'S') reindeer = Point(x, y)
                if (c == 'E') end = Point(x, y)

                grid[Point(x, y)] = c

            }
            grid
        }
        val visited = HashSet<DirectedPoint>()
        val queue = ArrayDeque<Pair<DirectedPoint, Long>>()
        val validPoints = HashSet<Point>()

        queue.add(reindeer to Direction.RIGHT to 0)
        while (queue.isNotEmpty()) {
            val (current, currentScore) = queue.removeFirst()
            validPoints.add(current.first)

            if (current.first == end) continue

            if (visited.contains(current)) continue
            visited.add(current)

            val nextMoves = listOf(
                current.first + current.second to current.second to 1,
                current.first + current.second.cw() to current.second.cw() to 1000,
                current.first + current.second.ccw() to current.second.ccw() to 1000
            )
            for ((next, cost) in nextMoves) {
                if (next.first !in grid || visited.contains(next)) continue
                val lowest = traverse(next, end, grid)
                if (currentScore + cost + lowest > target) continue

                val nextScore = currentScore + cost
                queue.add(next to nextScore)
            }
        }

        return validPoints.count()
    }

    //Part 1
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 7036)
    check(part2(testInput, 7036).also { it.println() } == 45)
    val input = readInput("Day16")
    val lowest = part1(input).also { it.println() }

    //Part 2
    part2(input, lowest).println()
}