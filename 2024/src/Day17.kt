import kotlin.math.pow


private data class Computer(var a: Long, var b: Long, var c: Long) {

    val output = mutableListOf<Long>()
    fun combo(op: Char): Long {
        return when (op) {
            '4' -> a
            '5' -> b
            '6' -> c
            else -> op.toString().toLong()
        }
    }

    fun operate(code: Char, op: Char, index: Int): Int {
        var next = index + 2
        when (code) {
            '0' -> a = (a / 2.toDouble().pow(combo(op).toDouble())).toLong()
            '1' -> b = b.xor(op.toString().toLong())
            '2' -> b = combo(op) % 8
            '3' -> if (a != 0L) next = op.toString().toInt()
            '4' -> b = b.xor(c)
            '5' -> output.add(combo(op) % 8)
            '6' -> b = (a / 2.toDouble().pow(combo(op).toDouble())).toLong()
            else -> c = (a / 2.toDouble().pow(combo(op).toDouble())).toLong()
        }
        return next
    }

    fun run(program: List<Char>): String {
        var index = 0
        while (index in program.indices) {
            index = operate(program[index], program[index + 1], index)
        }
        return output.joinToString(",")
    }
}

fun main() {
    fun part1(input: List<String>): String {
        val registers = input.takeWhile { it.isNotEmpty() }.map { it.split(": ").last().toLong() }
        val program = input.last().split(": ").last().split(",").joinToString("").toList()

        val computer = Computer(registers[0], registers[1], registers[2])

        return computer.run(program)
    }


    /**
     * Each loop through the program is independent, but the output is always derived from a % 8
     * So we can start from the end of the program and work backwards to find a given A that outputs the correct code
     * Once found, multiply A by 8 and move up a level
     */
    fun matchA(program: List<Char>, target: List<Char>): Long {
        var a = if (target.size == 1) 0 else 8 * matchA(program, target.subList(1, target.size))

        while (Computer(a, 0, 0).run(program) != target.joinToString(",")) {
            a++
        }
        return a
    }

    fun part2(input: List<String>): Long {
        val program = input.last().split(": ").last().split(",").joinToString("").toList()
        return matchA(program, program)
    }

    //Part 1
    val testInput = readInput("Day17_test")
    check(part1(testInput) == "4,6,3,5,6,3,5,2,1,0")
    val input = readInput("Day17")
    part1(input).println()

    //Part 2
    check(part2(readInput("Day17_test2")) == 117440L)
    part2(input).println()


}




