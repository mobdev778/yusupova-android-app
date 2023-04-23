package com.github.mobdev778.yusupova.designsystem.animatedtextview

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.SystemClock
import android.text.BoringLayout
import android.text.Layout
import android.text.Layout.Alignment
import android.text.TextPaint
import android.text.TextUtils
import com.github.mobdev778.yusupova.designsystem.animatedtextview.figure.Figure
import com.github.mobdev778.yusupova.designsystem.animatedtextview.figure.FigureBuilder
import com.github.mobdev778.yusupova.designsystem.animatedtextview.figure.Point
import kotlinx.coroutines.delay

@Suppress("TooManyFunctions")
internal data class AnimatedTextViewState(
    private val number: Int = 0,
    private val mode: AnimatedTextMode = AnimatedTextMode.DEFAULT,
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
    private var figureStartTime: LongArray = longArrayOf(),

    private val alphaValues: IntArray = IntArray(0)
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
        mode: AnimatedTextMode,
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
            mode = mode,
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

    suspend fun nextState(): AnimatedTextViewState {
        var bitmapChanged = false
        figures.forEachIndexed { index, figure ->
            if (SystemClock.elapsedRealtime() > figureStartTime[index]) {
                val pointsPerFrame = figure.size / FRAMES
                val pointsAdded = buildNewGeneration(index, pointsPerFrame)
                if (pointsAdded > 0) {
                    bitmapChanged = true
                }
            } else {
                // should update this figure in the future
                bitmapChanged = true
            }
        }

        return when {
            bitmapChanged -> {
                bitmap?.setPixels(letterPixels, 0, width, 0, 0, width, height)
                copy(number = number + 1)
            }
            mode == AnimatedTextMode.LEFT_TO_RIGHT_SHIMMER -> {
                val alphaValues = updateAlphaValues()
                copy(number = number + 1, alphaValues = alphaValues)
            }
            else -> {
                this
            }
        }
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
            point.generation = number - 1

            letterPixels[point.offset] = point.color
            pointsAdded++

            deque.addNeighbour(figure, point.x - 1, point.y)
            deque.addNeighbour(figure, point.x, point.y - 1)
            deque.addNeighbour(figure, point.x + 1, point.y)
            deque.addNeighbour(figure, point.x, point.y + 1)
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

    private suspend fun updateAlphaValues(): IntArray {
        val alphaValues = if (alphaValues.isEmpty()) {
            calculateAlphaValues()
        } else {
            this.alphaValues
        }

        delay(TRANSPARENCY_ANIMATION_DELAY)
        figures.forEach { figure ->
            figure.iterator().forEach { point ->
                val index = point.generation % alphaValues.size
                val newColor = point.getColor(alphaValues[index])
                letterPixels[point.offset] = newColor
            }
        }

        val lastAlpha = alphaValues[alphaValues.size - 1]
        for (i in alphaValues.size - 2 downTo  0) {
            alphaValues[i + 1] = alphaValues[i]
        }
        alphaValues[0] = lastAlpha
        bitmap?.setPixels(letterPixels, 0, width, 0, 0, width, height)
        return alphaValues
    }

    private fun calculateAlphaValues(): IntArray {
        var maxGeneration = -1
        figures.forEach { figure ->
            figure.iterator().forEach { point ->
                if (maxGeneration < point.generation) {
                    maxGeneration = point.generation
                }
            }
        }

        val size = getAlignedSize(maxGeneration * ALPHA_SEGMENTS)
        val alphaValues = IntArray(size)
        for (index in 0 until maxGeneration) {
            alphaValues[index] = MAX_ALPHA
            alphaValues[index + maxGeneration] = MAX_ALPHA - MAX_ALPHA * index / maxGeneration
            alphaValues[alphaValues.size - maxGeneration + index] = MAX_ALPHA * index / maxGeneration
        }
        return alphaValues
    }

    private fun getAlignedSize(calculatedSize: Int): Int {
        var nearestPowerOfTwo = 1
        while (nearestPowerOfTwo < calculatedSize) {
            nearestPowerOfTwo = nearestPowerOfTwo.shl(1)
        }
        return nearestPowerOfTwo
    }

    companion object {
        private const val TRANSPARENT_COLOR = 0x00000000
        private const val DELAY = 50
        private const val FRAMES = 15
        private const val SHUFFLE_STEP = 18
        private const val DEFAULT_QUEUE_SIZE = 100

        private const val MAX_ALPHA = 255
        private const val ALPHA_SEGMENTS = 4
        private const val TRANSPARENCY_ANIMATION_DELAY = 15L
    }
}
