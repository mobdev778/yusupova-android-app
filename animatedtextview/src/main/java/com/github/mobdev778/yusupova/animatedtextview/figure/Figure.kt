package com.github.mobdev778.yusupova.animatedtextview.figure

internal interface Figure {

    fun getPoint(x: Int, y: Int): Point?

    val size: Int

    fun getCenterPoint(): Point

    fun getStartPoint(): Point

    fun iterator(): Iterator<Point>
}
