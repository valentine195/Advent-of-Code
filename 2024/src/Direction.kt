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

    fun char(): Char = when (this) {
        UP -> '^'
        RIGHT -> '>'
        DOWN -> 'v'
        LEFT -> '<'
    }

    fun rotate(): Direction {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }

    fun cw(): Direction {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }

    fun ccw(): Direction {
        return when (this) {
            DOWN -> RIGHT
            RIGHT -> UP
            UP -> LEFT
            LEFT -> DOWN
        }
    }
}
