fun main() {
    fun B(x1: Long, x2: Long, y1: Long, y2: Long, z1: Long, z2: Long): Long {
        return (z2 * x1 - z1 * x2) / (y2 * x1 - y1 * x2)
    }

    fun A(b: Long, x1: Long, y1: Long, z1: Long): Long {
        return (z1 - b * y1) / x1
    }

    fun part1(input: List<String>): Long {
        /**
         * Button A: X_1, X_2
         * Button B: Y_1, Y_2
         * Prize: Z_1, Z_2
         *
         * X_1A + Y_1B = Z_1
         * X_2A + Y_2B = Z_2
         *
         * B = (Z_2 * X_1 - Z_1 * X_2) / (Y_2 * X_1 - Y_1 * X_2)
         * A = (Z_1 - B * Y_1) / X_1
         *
         * Tokens = 3*A + B
         */

        return input.filter { it.isNotEmpty() }.chunked(3).sumOf {
            val (_, x1, x2) = Regex("""X\+(\d+), Y\+(\d+)""").find(it[0])!!.groupValues
            val (_, y1, y2) = Regex("""X\+(\d+), Y\+(\d+)""").find(it[1])!!.groupValues
            val (_, z1, z2) = Regex("""X=(\d+), Y=(\d+)""").find(it[2])!!.groupValues
            val b = B(x1.toLong(), x2.toLong(), y1.toLong(), y2.toLong(), z1.toLong(), z2.toLong())
            val a = A(b, x1.toLong(), y1.toLong(), z1.toLong())

            if (a > 100 || b > 100 || x1.toLong() * a + y1.toLong() * b != z1.toLong() || x2.toLong() * a + y2.toLong() * b != z2.toLong()) 0L else 3 * a + b
        }
    }

    fun part2(input: List<String>): Long {
        return input.filter { it.isNotEmpty() }.chunked(3).sumOf {
            val (_, x1, x2) = Regex("""X\+(\d+), Y\+(\d+)""").find(it[0])!!.groupValues
            val (_, y1, y2) = Regex("""X\+(\d+), Y\+(\d+)""").find(it[1])!!.groupValues
            val (_, z1, z2) = Regex("""X=(\d+), Y=(\d+)""").find(it[2])!!.groupValues
            val b = B(
                x1.toLong(),
                x2.toLong(),
                y1.toLong(),
                y2.toLong(),
                z1.toLong() + 10_000_000_000_000,
                z2.toLong() + 10_000_000_000_000
            )
            val a = A(b, x1.toLong(), y1.toLong(), z1.toLong() + 10_000_000_000_000)

            /*println("A: $a, B: $b")
            println("*** $x1 * A + $y1 * B = ${z1.toLong() + 10_000_000_000_000}  ***")
            println("${x1.toLong() * a + y1.toLong() * b} = ${z1.toLong() + 10_000_000_000_000}")*/

            if (x1.toLong() * a + y1.toLong() * b != z1.toLong() + 10_000_000_000_000 || x2.toLong() * a + y2.toLong() * b != z2.toLong() + 10_000_000_000_000) 0L else 3L * a + b
        }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 480L)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}