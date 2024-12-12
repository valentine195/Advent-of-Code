fun main() {
    data class Region(var area: Int, var perimeter: Int, var points: MutableSet<Point>, var plant: Char)

    fun getRegions(grid: Grid): List<Region> {
        val visited: MutableSet<Point> = mutableSetOf()
        val regions: MutableList<Region> = mutableListOf()


        /**
         * For each point, do a BFS for the *same plant*
         * Each same-plant increases the area
         * Each non-same-plant neighbor increases the perimeter
         */
        for (point in grid) {
            if (!visited.contains(point)) {
                val plant = grid[point] ?: continue
                val region = Region(0, 0, mutableSetOf(), plant).also { regions.add(it) }

                val queue = ArrayDeque<Point>().also { it.add(point) }

                while (!queue.isEmpty()) {
                    val vertex = queue.removeFirst()
                    if (!visited.contains(vertex)) {
                        region.area++
                        visited.add(vertex)
                        region.points.add(vertex)
                        vertex.neighbors4().forEach {
                            if (grid[it] != plant) {
                                //boundary, increase perimeter
                                region.perimeter++
                            } else if (it !in visited) {
                                queue.add(it)
                            }
                        }
                    }
                }
            }
        }

        return regions.toList()
    }

    fun getSides(region: Set<Point>): Int {
        val perim = region.flatMap { point ->
            Direction.entries.mapNotNull { direction ->
                val next = point + direction
                if (next !in region) direction to point else null
            }
        }
        val h = perim
            .groupBy { it.first to it.second.y }
            .filterKeys { (direction, _) -> direction in listOf(Direction.UP, Direction.DOWN) }
            .values
            .map { points ->
                points.map { it.second }
                    .distinct()
                    .sortedBy { it.x }
            }
            .sumOf { sortedPoints ->
                sortedPoints.zipWithNext { a, b ->
                    if (a.x + 1 != b.x) 1 else 0
                }.sum() + 1
            }
        val v = perim
            .groupBy { it.first to it.second.x }
            .filterKeys { (direction, _) -> direction in listOf(Direction.LEFT, Direction.RIGHT) }
            .values
            .map { points ->
                points.map { it.second }
                    .distinct()
                    .sortedBy { it.y }
            }
            .sumOf { sortedPoints ->
                sortedPoints.zipWithNext { a, b ->
                    if (a.y + 1 != b.y) 1 else 0
                }.sum() + 1
            }

        return h + v
    }

    fun part1(regions: List<Region>): Int {
        return regions.sumOf { it.area * it.perimeter }
    }

    fun part2(regions: List<Region>): Int {
        return regions.sumOf { it.area * getSides(it.points) }
    }

// Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day12_test")
    val testRegions = getRegions(Grid(testInput))
    check(part1(testRegions) == 1930)
    check(part2(testRegions) == 1206)

// Read the input from the `src/Day01.txt` file.
    val input = readInput("Day12")
    val regions = getRegions(Grid(input))
    part1(regions).println()
    part2(regions).println()
}