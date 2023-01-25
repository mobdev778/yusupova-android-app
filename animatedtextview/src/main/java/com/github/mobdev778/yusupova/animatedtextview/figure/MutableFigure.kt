package com.github.mobdev778.yusupova.animatedtextview.figure

import android.graphics.Point

internal class MutableFigure : Figure {

    private val mutablePoints = mutableSetOf<Point>()
    private var mutableCenterPoint: Point? = null
    private var mutableStartPoint: Point? = null

    private var allX: Long = 0
    private var allY: Long = 0

    fun addPoint(x: Int, y: Int): Point {
        val point = Point(x, y)

        mutablePoints.add(point)
        allX += x
        allY += y

        mutableCenterPoint = null
        return point
    }

    override fun getPoints(): Set<Point> {
        return mutablePoints
    }

    override fun getCenterPoint(): Point? {
        if (mutableCenterPoint != null) {
            return mutableCenterPoint
        }

        val mathCenterX = (allX / mutablePoints.size)
        val mathCenterY = (allY / mutablePoints.size)

        var bestDist: Long = 0
        var bestPoint: Point? = null
        for (point in mutablePoints) {
            val dist =
                (point.x - mathCenterX) * (point.x - mathCenterX) +
                        (point.y - mathCenterY) * (point.y - mathCenterY)
            if (bestPoint == null || dist < bestDist) {
                bestDist = dist
                bestPoint = point
            }
        }
        mutableCenterPoint = bestPoint
        return bestPoint
    }

    override fun getStartPoint(): Point? {
        if (mutableStartPoint != null) {
            return mutableStartPoint
        }

        var bestX = 0
        var bestPoint: Point? = null
        for (point in mutablePoints) {
            if (bestPoint == null || point.x < bestX) {
                bestX = point.x
                bestPoint = point
            }
        }
        mutableStartPoint = bestPoint
        return bestPoint
    }

    fun toFigure(): Figure {
        return ImmutableFigure(mutablePoints, getCenterPoint(), getStartPoint())
    }
}
