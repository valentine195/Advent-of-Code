fun main() {

    fun part1(input: List<String>, target: Int): Int {
        val grid = Grid(input) { it != '#' }
        val path = grid.bfs(grid.find('E')!!, grid.find('S')!!)


        return path.indices.sumOf { startIndex ->
            (startIndex + target until path.size).count { endIndex ->
                val dist = path[startIndex].manhattan(path[endIndex])
                dist <= 2 && endIndex - startIndex - dist >= target
            }
        }
    }

    fun part2(input: List<String>, target: Int): Int {
        val grid = Grid(input) { it != '#' }

        val path = grid.bfs(grid.find('E')!!, grid.find('S')!!)

        return path.indices.sumOf { startIndex ->
            (startIndex + target until path.size).count { endIndex ->
                val dist = path[startIndex].manhattan(path[endIndex])
                dist <= 20 && endIndex - startIndex - dist >= target
            }
        }
    }

//Part 1
    val testInput = readInput("Day20_test")
    check(part1(testInput, 64) == 1)
    check(part1(testInput, 40) == 1 + 1)
    check(part1(testInput, 38) == 1 + 1 + 1)
    check(part1(testInput, 36) == 1 + 1 + 1 + 1)
    check(part1(testInput, 20) == 1 + 1 + 1 + 1 + 1)
    check(part1(testInput, 12) == 1 + 1 + 1 + 1 + 1 + 3)
    check(part1(testInput, 10) == 1 + 1 + 1 + 1 + 1 + 3 + 2)
    check(part1(testInput, 8) == 1 + 1 + 1 + 1 + 1 + 3 + 2 + 4)
    check(part1(testInput, 6) == 1 + 1 + 1 + 1 + 1 + 3 + 2 + 4 + 2)
    check(part1(testInput, 4) == 1 + 1 + 1 + 1 + 1 + 3 + 2 + 4 + 2 + 14)
    check(part1(testInput, 2) == 1 + 1 + 1 + 1 + 1 + 3 + 2 + 4 + 2 + 14 + 14)

    val input = readInput("Day20")
    part1(input, 100).println()

//Part 2
    check(part2(testInput, 76) == 3)
    check(part2(testInput, 74) == 3 + 4)
    check(part2(testInput, 72) == 3 + 4 + 22)
    part2(input, 100).println()
}