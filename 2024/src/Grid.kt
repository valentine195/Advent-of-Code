class Grid(input: List<String>, filter: ((Char) -> Boolean) = { true }) {

    private val charMap: MutableMap<Char, MutableList<Point>> = mutableMapOf()
    private lateinit var bounds: Point
    private val grid: HashMap<Point, Char> = input.foldIndexed(hashMapOf()) { y, grid, row ->
        row.toCharArray().forEachIndexed { x, c ->
            if (filter(c)) {
                grid[Point(x, y)] = c
                charMap.getOrPut(c) { mutableListOf() }.add(Point(x, y))
            }
        }

        bounds = Point(input[0].length, input.size)

        grid

    }

    fun getByChar(c: Char): List<Point> = charMap.getOrDefault(c, listOf()).toList()

    operator fun get(point: Point) = grid[point]

    operator fun set(point: Point, value: Char) {
        grid[point] = value
    }

    fun find(char: Char): Point? = grid.entries.find { it.value == char }?.key
    fun findAll(char: Char): List<Point> = grid.filter { it.value == char }.keys.toList()
    operator fun contains(point: Point) = grid.contains(point)


    fun bfs(start: Point, end: Point): List<Point> {
        val queue = ArrayDeque<Point>().also { it.add(start) }
        val cameFrom = hashMapOf<Point, Point>()

        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            if (next == end) break
            for (neighbor in next.neighbors4()
                .filter { it !in cameFrom && it in grid && it.x in 0..bounds.x && it.y in 0..bounds.y }) {

                cameFrom[neighbor] = next
                queue.add(neighbor)
            }
        }
        val path = mutableListOf<Point>()
        var point = end
        while (point != start) {
            path.add(point)
            if (point !in cameFrom) return listOf()
            point = cameFrom[point]!!
        }
        path.add(start)

        return path.toList()
    }

    operator fun iterator() = grid.keys.iterator()

    override fun toString(): String {
        val str = mutableListOf<String>()
        for (y in 0..bounds.y) {
            val line = mutableListOf<Char>()
            for (x in 0..bounds.x) {
                line.add(
                    grid.getOrDefault(Point(x, y), ' ')
                )
            }
            str.add(line.joinToString(""))
        }
        return str.joinToString("\n")
    }

    fun printPath(path: Map<Point, Char>) {
        val str = mutableListOf<String>()
        for (y in 0..bounds.y) {
            val line = mutableListOf<Char>()
            for (x in 0..bounds.x) {
                line.add(
                    path.getOrDefault(Point(x, y), grid.getOrDefault(Point(x, y), ' '))

                )
            }
            str.add(line.joinToString(""))
        }
        str.joinToString("\n").println()
    }

    fun printPath(path: List<Point>) {
        val str = mutableListOf<String>()
        for (y in 0..bounds.y) {
            val line = mutableListOf<Char>()
            for (x in 0..bounds.x) {
                line.add(
                    if (Point(x, y) in path) {
                        'O'
                    } else {
                        grid.getOrDefault(Point(x, y), ' ')
                    }
                )
            }
            str.add(line.joinToString(""))
        }
        str.joinToString("\n").println()
    }
}