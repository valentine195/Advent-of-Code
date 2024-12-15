class Grid(input: List<String>) {
    private val charMap: MutableMap<Char, MutableList<Point>> = mutableMapOf()
    private lateinit var bounds: Point
    private val grid: HashMap<Point, Char> = input.foldIndexed(hashMapOf()) { y, grid, row ->
        row.toCharArray().forEachIndexed { x, c ->
            grid[Point(x, y)] = c
            charMap.getOrPut(c) { mutableListOf() }.add(Point(x, y))
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
    fun contains(point: Point) = grid.contains(point)
    fun isInside(point: Point) = point.x in 0..bounds.x && point.y in 0..bounds.y

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
}