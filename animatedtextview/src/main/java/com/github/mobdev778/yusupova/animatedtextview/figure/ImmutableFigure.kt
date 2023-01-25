package com.github.mobdev778.yusupova.animatedtextview.figure

import android.graphics.Point

internal class ImmutableFigure(
    private val points: Set<Point>,
    private val centerPoint: Point?,
    private val startPoint: Point?
) : Figure {

    override fun getPoints(): Set<Point> {
        return points
    }

    override fun getCenterPoint(): Point? {
        return centerPoint
    }

    override fun getStartPoint(): Point? {
        return startPoint
    }
}
