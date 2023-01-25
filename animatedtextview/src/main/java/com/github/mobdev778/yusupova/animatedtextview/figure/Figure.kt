package com.github.mobdev778.yusupova.animatedtextview.figure

import android.graphics.Point

internal interface Figure {

    fun getPoints(): Set<Point>

    fun getCenterPoint(): Point?

    fun getStartPoint(): Point?
}
