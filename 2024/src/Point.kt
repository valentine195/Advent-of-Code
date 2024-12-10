class Point(val x: Int, val y: Int) {

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
    override fun equals(other: Any?): Boolean {
        return other is Point && other.x == x && other.y == y
    }

    override fun toString(): String {
        return "(x: ${x}, y: ${y})"
    }

    fun add(point: Point): Point {
        return Point(point.x + x, point.y + y)
    }

    fun subtract(point: Point): Point {
        return Point(point.x - x, point.y - y)
    }

    fun distance(point: Point): Point {
        return Point(point.x - x, point.y - y)
    }

    fun neighbors4(): List<Point> {
        return listOf(
            add(Point(0, -1)),
            add(Point(1, 0)),
            add(Point(0, 1)),
            add(Point(-1, 0)),
        )
    }

    fun neighbors8(): List<Point> {
        return listOf(
            add(Point(0, -1)),
            add(Point(1, -1)),
            add(Point(1, 0)),
            add(Point(1, 1)),
            add(Point(0, 1)),
            add(Point(-1, 1)),
            add(Point(-1, 0)),
            add(Point(-1, -1)),
        )
    }

}