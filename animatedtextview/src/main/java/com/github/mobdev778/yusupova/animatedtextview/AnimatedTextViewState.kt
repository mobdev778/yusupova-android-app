package com.github.mobdev778.yusupova.animatedtextview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.SystemClock
import android.text.BoringLayout
import android.text.Layout
import android.text.Layout.Alignment
import android.text.TextPaint
import android.text.TextUtils
import com.github.mobdev778.yusupova.animatedtextview.figure.Figure
import com.github.mobdev778.yusupova.animatedtextview.figure.FigureBuilder
import com.github.mobdev778.yusupova.animatedtextview.figure.Point

@Suppress("TooManyFunctions")
internal data class AnimatedTextViewState(
    private var number: Int = 0,
    private var width: Int = 0,
    private var height: Int = 0,
    private var text: String = "",
    private var textPaint: TextPaint? = null,
    private var layout: Layout? = null,

    private var originalPixels: IntArray = intArrayOf(),
    private var letterPixels: IntArray = intArrayOf(),
    private var bitmap: Bitmap? = null,

    private var figures: List<Figure> = mutableListOf(),
    private var pointQueues: MutableList<ArrayDeque<Point>> = mutableListOf(),
    private var figureStartTime: LongArray = longArrayOf()
) {

    constructor() : this(0)

    fun reset(): AnimatedTextViewState {
        bitmap?.recycle()
        bitmap = null
        return copy(number = 0, figures = arrayListOf(), bitmap = null)
    }

    fun isInitialized(): Boolean {
        return figures.isNotEmpty()
    }

    fun initialize(
        text: String,
        width: Int,
        height: Int,
        textPaint: TextPaint,
        alignment: Alignment
    ): AnimatedTextViewState {
        val layout = getLayout(text, width, textPaint, alignment)

        // 1) draw text into originalBitmap
        val originalPixels = getOriginalPixels(text, width, height, layout, textPaint)

        // 2) calculate figures
        figures = FigureBuilder()
            .text(text)
            .paint(textPaint)
            .layout(layout)
            .width(width)
            .height(height)
            .build()

        // 3) draw first points
        val bitmap = createBitmap(width, height)
        letterPixels = bitmap.toIntArray()
        this.bitmap = bitmap
        val initialPointQueues = mutableListOf<ArrayDeque<Point>>()
        figures.forEach { figure ->
            val pointQueue = ArrayDeque<Point>(DEFAULT_QUEUE_SIZE)
            val startPoint = figure.getStartPoint()
            pointQueue.add(startPoint)
            initialPointQueues.add(pointQueue)
        }
        pointQueues = initialPointQueues

        // 4) postpone next "onDraw()" call
        figureStartTime = LongArray(figures.size) { index ->
            SystemClock.elapsedRealtime() + index * DELAY
        }

        return copy(
            number = number + 1,
            width = width,
            height = height,
            text = text,
            textPaint = textPaint,
            layout = layout,
            originalPixels = originalPixels,
            figures = figures,
            pointQueues = pointQueues,
            figureStartTime = figureStartTime
        )
    }

    fun getBitmap(): Bitmap? {
        return bitmap
    }

    fun nextState(): AnimatedTextViewState {
        var bitmapChanged = false
        figures.forEachIndexed { index, figure ->
            if (SystemClock.elapsedRealtime() > figureStartTime[index]) {
                val pointsPerFrame = figure.size / FRAMES
                val pointsAdded = buildNewGeneration(index, pointsPerFrame)
                if (pointsAdded > 0) {
                    bitmapChanged = true
                }
            }
        }

        if (!bitmapChanged) {
            return this
        }

        bitmap?.setPixels(letterPixels, 0, width, 0, 0, width, height)
        return copy(
            number = number + 1,
            bitmap = bitmap
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AnimatedTextViewState

        if (number != other.number) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        var result = number
        result = 31 * result + width
        result = 31 * result + height
        result = 31 * result + text.hashCode()
        return result
    }

    private fun getLayout(text: String, width: Int, textPaint: TextPaint, alignment: Alignment): Layout {
        val metrics = BoringLayout.Metrics()
        with (textPaint.fontMetrics) {
            metrics.width = width
            metrics.ascent = ascent.toInt()
            metrics.descent = descent.toInt()
            metrics.top = top.toInt()
            metrics.bottom = bottom.toInt()
            metrics.leading = leading.toInt()
        }

        return BoringLayout.make(
            text,
            textPaint,
            width,
            alignment,
            1f,
            1f,
            metrics,
            true,
            TextUtils.TruncateAt.END,
            width
        )
    }

    private fun getOriginalPixels(
        text: String, width: Int, height: Int, layout: Layout, textPaint: TextPaint
    ): IntArray {
        val originalBitmap = createBitmap(width, height)
        val canvas = Canvas(originalBitmap)
        text.forEachIndexed { index, letter ->
            val lineIndex = layout.getLineForOffset(index)
            val left = layout.getPrimaryHorizontal(index)
            val baseLineY = layout.getLineBaseline(lineIndex).toFloat()
            canvas.drawText(letter.toString(), left, baseLineY, textPaint)
        }
        val pixels = originalBitmap.toIntArray()
        originalBitmap.recycle()
        return pixels
    }

    private fun createBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(TRANSPARENT_COLOR)
        return bitmap
    }

    private fun Bitmap.toIntArray(): IntArray {
        val pixels = IntArray(width * height)
        getPixels(pixels, 0, width, 0, 0, width, height)
        return pixels
    }

    @Suppress("CyclomaticComplexMethod")
    private fun buildNewGeneration(index: Int, pointsPerFrame: Int): Int {
        var pointsAdded = 0
        val deque = pointQueues[index]
        val figure = figures[index]
        while (pointsAdded < pointsPerFrame && deque.isNotEmpty()) {
            if (pointsAdded % SHUFFLE_STEP == 0) {
                deque.shuffle()
            }
            val point = deque.removeFirst()
            letterPixels[point.offset] = point.color
            pointsAdded++

            if (point.x > 0) {
                deque.addNeighbour(figure, point.x - 1, point.y)
            }
            if (point.y > 0) {
                deque.addNeighbour(figure, point.x, point.y - 1)
            }
            if (point.x + 1 < width) {
                deque.addNeighbour(figure, point.x + 1, point.y)
            }
            if (point.y + 1 < height) {
                deque.addNeighbour(figure, point.x, point.y + 1)
            }
        }
        bitmap?.setPixels(letterPixels, 0, width, 0, 0, width, height)
        return pointsAdded
    }

    private fun ArrayDeque<Point>.addNeighbour(figure: Figure, x: Int, y: Int) {
        val point = figure.getPoint(x, y)
        if (point != null && !point.visited) {
            add(point)
            point.visited = true
        }
    }

    private companion object {
        const val TRANSPARENT_COLOR = 0x00000000
        const val DELAY = 50
        const val FRAMES = 18
        const val SHUFFLE_STEP = 18
        const val DEFAULT_QUEUE_SIZE = 100
    }
}
