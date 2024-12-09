fun main() {
    abstract class SizedMemory(open var size: Int) {
        abstract fun print(): String
    }

    data class File(override var size: Int, val id: Int) : SizedMemory(size) {
        override fun print(): String {
            return "[${this.id.toString().repeat(this.size)}]"
        }
    }

    data class Gap(override var size: Int) : SizedMemory(size) {
        override fun print(): String {
            return ".".repeat(this.size)
        }
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            val blocks: MutableList<SizedMemory> = line.map { it.toString().toInt() }.chunked(2).mapIndexed { i, it ->
                listOfNotNull(
                    File(it[0], i), if (it.size > 1) Gap(it[1]) else null
                )

            }.flatten().toMutableList()
            var idx = 0
            label@ while (idx < blocks.size) {
                when (val gap = blocks[idx]) {
                    is File -> idx++
                    is Gap -> {
                        if (gap.size == 0) {
                            idx++
                        } else {
                            while (gap.size > 0) {
                                val lastIndex = blocks.indexOfLast { it is File }
                                if (lastIndex < idx) break@label
                                val block = blocks[lastIndex] as File
                                if (block.size < gap.size) {
                                    gap.size -= block.size
                                    blocks.removeAt(lastIndex)
                                    blocks.add(idx, block)
                                    idx++
                                } else {
                                    blocks[idx] = File(gap.size, block.id)
                                    block.size -= gap.size
                                    if (block.size == 0) blocks.removeAt(lastIndex)
                                    break
                                }
                            }
                        }
                    }
                }
            }

            var sum = 0L
            var index = 0
            for (block in blocks) {
                if (block is File) {
                    for (i in 1..block.size) {
                        sum += block.id * index
                        index++
                    }
                }
            }
            sum
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { line ->
            val blocks: MutableList<SizedMemory> = line.map { it.toString().toInt() }.chunked(2).mapIndexed { i, it ->
                listOfNotNull(
                    File(it[0], i), if (it.size > 1) Gap(it[1]) else null
                )

            }.flatten().toMutableList()

            for (idx in blocks.indices.reversed()) {
                when (val file = blocks[idx]) {
                    is File -> {
                        val gapIndex = blocks.slice(0..<idx).indexOfFirst { it is Gap && it.size >= file.size }
                        if (gapIndex < 0) continue
                        val gap = blocks[gapIndex] as Gap
                        if (file.size == gap.size) {
                            blocks[gapIndex] = File(file.size, file.id)
                            blocks[idx] = Gap(file.size)
                        } else {
                            gap.size -= file.size
                            blocks[idx] = Gap(file.size)
                            blocks.add(gapIndex, File(file.size, file.id))
                        }
                    }

                    is Gap -> continue

                }
            }

            var sum = 0L
            var index = 0
            for (block in blocks) {
                for (i in 1..block.size) {
                    if (block is File) {
                        sum += block.id * index
                    }
                    index++

                }
            }
            sum
        }
    }


// Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

// Read the input from the `src/Day01.txt` file.
    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}