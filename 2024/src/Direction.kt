enum class Direction() {
    RIGHT, DOWN, LEFT, UP;

    fun move(): Point {
        return when (this) {
            UP -> Point(0, -1)
            RIGHT -> Point(1, 0)
            DOWN -> Point(0, 1)
            LEFT -> Point(-1, 0)
        }
    }

    fun others(): List<Direction> = entries.filter { it != this }

    fun rotate(): Direction {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }
}
