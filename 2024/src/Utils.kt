import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInputText(name: String): String = Path("./2024/src/$name.txt").readText().trim()

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> = readInputText(name).lines()


/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

class Grid(input: List<String>) {
    private val grid: HashMap<Point, Char> = input.foldIndexed(hashMapOf<Point, Char>()) { y, grid, row ->
        row.toCharArray().forEachIndexed { x, c ->
            grid[Point(x, y)] = c
        }
        grid
    }

    operator fun get(point: Point) = grid[point]

    operator fun set(point: Point, value: Char) {
        grid[point] = value
    }

    fun find(char: Char): Point? = grid.entries.find { it.value == char }?.key
    fun contains(point: Point) = grid.contains(point)
}
