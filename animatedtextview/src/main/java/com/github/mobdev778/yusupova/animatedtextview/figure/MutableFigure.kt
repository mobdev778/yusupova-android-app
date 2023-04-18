package com.github.mobdev778.yusupova.animatedtextview.figure

internal class MutableFigure : Figure {

    private val points: XYMap<Point> = XYMap()
    private var mutableCenterPoint: Point? = null
    private var mutableStartPoint: Point? = null

    private var allX: Long = 0
    private var allY: Long = 0

    fun addPoint(point: Point) {
        points.put(point.x, point.y, point)
        allX += point.x
        allY += point.y

        mutableCenterPoint = null
    }

    override fun getPoint(x: Int, y: Int): Point? {
        return points.get(x, y)
    }

    override val size: Int = points.size()

    override fun getCenterPoint(): Point {
        if (mutableCenterPoint != null) {
            return mutableCenterPoint!!
        }

        val mathCenterX = (allX / points.size())
        val mathCenterY = (allY / points.size())

        var bestDist: Long = 0
        var bestPoint: Point? = null

        points.valueIterator().forEach { point ->
            val dist =
                (point.x - mathCenterX) * (point.x - mathCenterX) +
                        (point.y - mathCenterY) * (point.y - mathCenterY)
            if (bestPoint == null || dist < bestDist) {
                bestDist = dist
                bestPoint = point
            }
        }
        mutableCenterPoint = bestPoint
        return bestPoint!!
    }

    override fun getStartPoint(): Point {
        if (mutableStartPoint != null) {
            return mutableStartPoint!!
        }

        var bestX = 0
        var bestPoint: Point? = null
        points.valueIterator().forEach { point ->
            if (bestPoint == null || point.x < bestX) {
                bestX = point.x
                bestPoint = point
            }
        }
        mutableStartPoint = bestPoint
        return bestPoint!!
    }

    override fun iterator(): Iterator<Point> {
        return points.valueIterator()
    }

    fun toFigure(): Figure {
        return ImmutableFigure(points, getCenterPoint(), getStartPoint())
    }
}
