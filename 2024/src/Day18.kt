fun main() {
    fun bfs(bits: List<Point>, bounds: Point): List<Point> {
        val queue = ArrayDeque<Point>().also { it.add(Point(0, 0)) }
        val cameFrom = hashMapOf<Point, Point>()

        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            if (next == bounds) break
            for (neighbor in next.neighbors4()
                .filter { it !in cameFrom && it !in bits && it.x in 0..bounds.x && it.y in 0..bounds.y }) {

                cameFrom[neighbor] = next
                queue.add(neighbor)
            }
        }
        val path = mutableListOf<Point>()
        var point = bounds
        while (point != Point(0, 0)) {
            path.add(point)
            if (point !in cameFrom) return listOf()
            point = cameFrom[point]!!
        }

        return path.toList()
    }

    fun part1(input: List<String>, count: Int, bounds: Point): Int {
        val bits = input.slice(0 until count).map { it.split(",").let { Point(it[0].toInt(), it[1].toInt()) } }

        return bfs(bits, bounds).size
    }

    fun part2(input: List<String>, start: Int, bounds: Point): String {
        val bits =
            input.map { it.split(",").let { Point(it[0].toInt(), it[1].toInt()) } }
        var check = start + (input.size - start) / 2
        val checked = mutableSetOf<Int>()
        while (true) {
            val path = bfs(bits.slice(0 until check), bounds)
            if (path.isEmpty() && bfs(bits.slice(0 until check - 1), bounds).isNotEmpty()) {
                //found it
                val p = bits[check - 1]
                return "${p.x},${p.y}"
            }
            if (path.isNotEmpty() && bfs(bits.slice(0 until check + 1), bounds).isEmpty()) {
                //found it
                val p = bits[check]
                return "${p.x},${p.y}"
            }
            if (path.isNotEmpty()) {
                //move right
                check += (input.size - check) / 2 + (input.size - 1) % 2
            } else {
                //move left
                check -= (input.size - check) / 2 + (input.size - 1) % 2
            }
            if (!checked.add(check)) {
                throw Error("Loop")
            }
        }
    }

    //Part 1
    val testInput = readInput("Day18_test")
    check(part1(testInput, 12, Point(6, 6)) == 22)
    val input = readInput("Day18")
    part1(input, 1024, Point(70, 70)).println()

    //Part 2
    check(part2(testInput, 12, Point(6, 6)).also { it.println() } == "6,1")
    part2(input, 1024, Point(70, 70)).println()
}