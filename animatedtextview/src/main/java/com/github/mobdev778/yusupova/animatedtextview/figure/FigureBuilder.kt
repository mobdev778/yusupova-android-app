package com.github.mobdev778.yusupova.animatedtextview.figure

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.TextPaint

internal class FigureBuilder {

    private var width: Int = -1
    private var height: Int = -1
    private lateinit var textPaint: TextPaint
    private lateinit var text: CharSequence
    private lateinit var layout: Layout

    fun paint(paint: Paint) = apply {
        this.textPaint = TextPaint(paint).apply {
            isAntiAlias = true
        }
    }

    fun text(text: CharSequence) = apply {
        this.text = text
    }

    fun layout(layout: Layout) = apply {
        this.layout = layout
    }

    fun width(width: Int) = apply {
        this.width = width
    }

    fun height(height: Int) = apply {
        this.height = height
    }

    fun build(): List<Figure> {
        check(width > 0)
        check(height > 0)

        val figures = mutableListOf<Figure>()

        val maxWidth = width
        val bitmap = Bitmap.createBitmap(maxWidth, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.drawARGB(0xFF, 0x00, 0x00, 0xFF)

        val redPaint = Paint(textPaint).apply { color = Color.RED }
        val greenPaint = Paint(textPaint).apply { color = Color.GREEN }

        text.forEachIndexed { index, letter ->
            val paint = if (index % 2 == 0) redPaint else greenPaint
            val lineIndex = layout.getLineForOffset(index)
            val left = layout.getPrimaryHorizontal(index)
            val baseLineY = layout.getLineBaseline(lineIndex).toFloat()
            canvas.drawText(letter.toString(), left, baseLineY, paint)
        }

        val bitmapPixels = IntArray(maxWidth * bitmap.height)
        bitmap.getPixels(bitmapPixels, 0, maxWidth, 0, 0, maxWidth, bitmap.height)
        bitmap.recycle()

        val widths = FloatArray(1)
        text.forEachIndexed { index, letter ->
            val paint = if (index % 2 == 0) redPaint else greenPaint
            val lineIndex = layout.getLineForOffset(index)
            val left = layout.getPrimaryHorizontal(index).toInt()

            paint.getTextWidths(letter.toString(), widths)
            val letterWidth = (widths[0] * 1.1f).toInt() // * 1.1f - fix for bold italic letters
            val top = layout.getLineTop(lineIndex)
            val bottom = layout.getLineBottom(lineIndex).coerceAtMost(height)
            val right = (left + letterWidth).coerceAtMost(maxWidth)

            val letterFigures = calculateFigures(
                bitmapPixels, maxWidth,
                left, top, right - left, bottom - top,
                paint.color
            )
            figures.addAll(letterFigures)
        }
        return figures
    }

    private fun calculateFigures(
        bitmapPixels: IntArray, bitmapWidth: Int,
        startX: Int, startY: Int, width: Int, height: Int,
        figureColor: Int
    ): List<Figure> {
        val markers = Array<Array<Marker?>>(height) {
            Array(width) { null }
        }

        var number = 1
        var currentMarker = Marker(number)

        var successiveIncrements = 0
        for (y in 0 until height) {
            val yOffset = (y + startY) * bitmapWidth
            for (x in 0 until width) {
                val offset = yOffset + startX + x
                if (offset < bitmapPixels.size) {
                    val color = bitmapPixels[yOffset + startX + x]
                    if (color == figureColor) {
                        successiveIncrements = 0

                        val nw = if (x > 0 && y > 0) {
                            markers[y - 1][x - 1]
                        } else null
                        val n = if (y > 0) {
                            markers[y - 1][x]
                        } else null
                        val ne = if (x < width - 1 && y > 0) {
                            markers[y - 1][x + 1]
                        } else null
                        val w = if (x > 0) {
                            markers[y][x - 1]
                        } else null

                        val minMarker = minMarker(nw, n, ne, w, currentMarker)
                        markers[y][x] = minMarker
                        nw?.number = minMarker.number
                        n?.number = minMarker.number
                        ne?.number = minMarker.number
                        w?.number = minMarker.number
                        currentMarker = minMarker
                    } else {
                        if (successiveIncrements < 1) {
                            successiveIncrements++
                            number++
                            currentMarker = Marker(number)
                        }
                    }
                }
            }
        }

        val figures = mutableListOf<Figure>()
        val markerFigureMap = mutableMapOf<Int, MutableFigure>()
        for (y in 0 until height) {
            for (x in 0 until width) {
                val marker = markers[y][x]
                if (marker != null) {
                    val oldFigure = markerFigureMap[marker.number]
                    if (oldFigure != null) {
                        oldFigure.addPoint(startX + x, startY + y)
                    } else {
                        val newFigure = MutableFigure()
                        newFigure.addPoint(startX + x, startY + y)
                        markerFigureMap[marker.number] = newFigure
                    }
                }
            }
        }
        markerFigureMap.values.forEach {
            figures.add(it.toFigure())
        }
        return figures
    }

    private fun minMarker(vararg markers: Marker?): Marker {
        var minMarker: Marker? = null
        for (tempMarker in markers) {
            if (minMarker == null) {
                minMarker = tempMarker
            } else {
                if (tempMarker != null && tempMarker.number < minMarker.number) {
                    minMarker = tempMarker
                }
            }
        }
        return minMarker!!
    }
}
