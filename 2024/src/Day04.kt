fun main() {
    val neighbors = listOf(
        Pair(-1, -1),
        Pair(-1, 0),
        Pair(-1, 1),
        Pair(0, -1),
        Pair(0, 1),
        Pair(1, -1),
        Pair(1, 0),
        Pair(1, 1),
    )

    fun add(pair1: Pair<Int, Int>, pair2: Pair<Int, Int>): Pair<Int, Int> {
        return Pair(pair1.first + pair2.first, pair1.second + pair2.second)
    }

    fun part1(input: List<String>): Int {
        //x, y
        val grid: HashMap<Pair<Int, Int>, Char> = hashMapOf()
        val xLocations: HashSet<Pair<Int, Int>> = hashSetOf()
        for (i in -1..input[0].length) {
            grid[Pair(i, -1)] = 'Z'
            grid[Pair(i, input.size)] = 'Z'
        }
        input.forEachIndexed { y, row ->
            row.toCharArray().forEachIndexed { x, c ->
                grid[Pair(x, y)] = c
                if (c == 'X') xLocations.add(Pair(x, y))
            }
        }

        var count = 0
        for (x in xLocations) {
            for (neighbor in neighbors) {
                if (grid[add(x, neighbor)] != 'M') continue
                if (grid[add(x, add(neighbor, neighbor))] != 'A') continue
                if (grid[add(x, add(neighbor, add(neighbor, neighbor)))] != 'S') continue
                count++
            }
        }

        return count
    }

    fun part2(input: List<String>): Int {
        //x, y
        val grid: HashMap<Pair<Int, Int>, Char> = hashMapOf()
        val aLocations: HashSet<Pair<Int, Int>> = hashSetOf()
        for (i in -1..input[0].length) {
            grid[Pair(i, -1)] = 'Z'
            grid[Pair(i, input.size)] = 'Z'
        }
        input.forEachIndexed { y, row ->
            row.toCharArray().forEachIndexed { x, c ->
                grid[Pair(x, y)] = c
                if (c == 'A') aLocations.add(Pair(x, y))
            }
        }
        var count = 0
        for (loc in aLocations) {
            val ms = listOf('M', 'S')
            // \ first
            val first = setOf(grid[add(loc, Pair(-1, -1))], grid[add(loc, Pair(1, 1))])
            if (first.size != 2 || !first.all { ms.contains(it) }) continue

            // / second
            val second = setOf(grid[add(loc, Pair(1, -1))], grid[add(loc, Pair(-1, 1))])
            if (second.size != 2 || !second.all { ms.contains(it) }) continue

            count++
        }
        return count
    }


    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}