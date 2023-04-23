package com.github.mobdev778.yusupova.animatedsplashview

import android.graphics.Bitmap
import android.graphics.Color
import com.github.mobdev778.yusupova.animatedsplashview.figure.Point
import com.github.mobdev778.yusupova.animatedsplashview.figure.XYMap
import kotlinx.coroutines.delay

@Suppress("TooManyFunctions")
internal data class AnimatedSplashState(
    private val number: Int = 0,
    private val mode: AnimatedSplashMode = AnimatedSplashMode.DEFAULT,
    private val width: Int = 0,
    private val height: Int = 0,
    private val originalBitmap: Bitmap? = null,
    private val bitmap: Bitmap? = null,
    private val bitmapPixels: IntArray = IntArray(0),
    private val visitedPoints: XYMap<Point> = XYMap(),
    private val edgePoints: ArrayDeque<Point> = ArrayDeque(DEFAULT_QUEUE_SIZE),
    private val pointsPerFrame: Int = 0,
    private val alphaValues: IntArray = IntArray(0)
) {
    fun isInitialized(): Boolean {
        return number > 0
    }

    override fun equals(other: Any?): Boolean {
        if (other is AnimatedSplashState) {
            return number == other.number &&
                    width == other.width &&
                    height == other.height
        }
        return false
    }

    override fun hashCode(): Int {
        var result = number
        result = 31 * result + width
        result = 31 * result + height
        return result
    }

    fun initialize(
        originalBitmap: Bitmap,
        mode: AnimatedSplashMode
    ): AnimatedSplashState {
        val pair = originalBitmap.getStartPoint()
        val startPoint = pair.first
        val pointsPerFrame = pair.second / ANIMATED_SPLASH_VIEW_FRAMES

        val bitmap = createBitmap(originalBitmap.width, originalBitmap.height)
        bitmap.setPixel(startPoint.x, startPoint.y, startPoint.color)

        val edgePoints = ArrayDeque<Point>(DEFAULT_QUEUE_SIZE)
        edgePoints.add(startPoint)

        return copy(
            number = 1,
            mode = mode,
            width = originalBitmap.width,
            height = originalBitmap.height,
            originalBitmap = originalBitmap,
            bitmap = bitmap,
            bitmapPixels = bitmap.toIntArray(),
            visitedPoints = XYMap(),
            edgePoints = edgePoints,
            pointsPerFrame = pointsPerFrame,
            alphaValues = alphaValues
        )
    }

    fun getBitmap(): Bitmap? {
        return bitmap
    }

    suspend fun nextState(): AnimatedSplashState {
        val pointsAdded = buildNewGeneration(pointsPerFrame)

        return when {
            pointsAdded > 0 -> {
                copy(number = number + 1)
            }
            pointsAdded == 0 && mode == AnimatedSplashMode.CLOCKWISE_SHIMMER -> {
                val alphaValues = updateAlphaValues()
                copy(number = number + 1, alphaValues = alphaValues)
            }
            else -> {
                this
            }
        }
    }

    @Suppress("NestedBlockDepth")
    private fun Bitmap.getStartPoint(): Pair<Point, Int> {
        var pixelCount = 0
        val pixels = toIntArray()

        var startX: Int = -1
        var startY: Int = -1
        for (y in 0 until height) {
            val lineOffset = y * width
            for (x in 0 until width) {
                val offset = lineOffset + x
                val color = pixels[offset]
                if (color != Color.TRANSPARENT) {
                    pixelCount++
                    if (x > startX || x == startX && y > startY) {
                        startX = x
                        startY = y
                    }
                }
            }
        }

        check(startX > -1) { "No pixels found" }
        val offset = startY * width + startX
        return Pair(Point(startX, startY, offset, pixels[offset], 0), pixelCount)
    }

    private fun buildNewGeneration(pointsPerFrame: Int): Int {
        var pointsAdded = 0

        while (pointsAdded < pointsPerFrame && edgePoints.isNotEmpty()) {
            val point = edgePoints.removeFirst()
            point.generation = number - 1
            pointsAdded++
            bitmapPixels[point.offset] = point.color

            if (pointsAdded % SHUFFLE_STEP == 0) {
                edgePoints.shuffle()
            }

            addNeighbour(point.x - 1, point.y - 1)
            addNeighbour(point.x, point.y - 1)
            addNeighbour(point.x + 1, point.y - 1)

            addNeighbour(point.x - 1, point.y)
            addNeighbour(point.x + 1, point.y)

            addNeighbour(point.x - 1, point.y + 1)
            addNeighbour(point.x, point.y + 1)
            addNeighbour(point.x + 1, point.y + 1)
        }

        bitmap?.setPixels(bitmapPixels, 0, width, 0, 0, width, height)
        return pointsAdded
    }

    private suspend fun updateAlphaValues(): IntArray {
        val alphaValues = if (alphaValues.isEmpty()) {
            calculateAlphaValues()
        } else {
            this.alphaValues
        }

        delay(TRANSPARENCY_ANIMATION_DELAY)
        visitedPoints.valueIterator().forEach { point ->
            val index = point.generation % alphaValues.size
            val alpha = alphaValues[index]
            val newColor = point.getColor(alpha)
            bitmapPixels[point.offset] = newColor
        }

        val lastAlpha = alphaValues[alphaValues.size - 1]
        for (i in alphaValues.size - 2 downTo  0) {
            alphaValues[i + 1] = alphaValues[i]
        }
        alphaValues[0] = lastAlpha
        bitmap?.setPixels(bitmapPixels, 0, width, 0, 0, width, height)
        return alphaValues
    }

    private fun createBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.TRANSPARENT)
        return bitmap
    }

    private fun Bitmap.toIntArray(): IntArray {
        val pixels = IntArray(width * height)
        getPixels(pixels, 0, width, 0, 0, width, height)
        return pixels
    }

    @Suppress("ComplexCondition")
    private fun addNeighbour(x: Int, y: Int) {
        val neighbour = visitedPoints.get(x, y)
        if (neighbour != null) {
            return
        }
        if (x >= 0 && y >= 0 && x < width && y < height) {
            val color = originalBitmap?.getPixel(x, y)
            if (color != Color.TRANSPARENT && color != null) {
                val newPoint = Point(x, y, y * width + x, color)
                visitedPoints.put(x, y, newPoint)
                edgePoints.addLast(newPoint)
            }
        }
    }

    private fun calculateAlphaValues(): IntArray {
        var maxGeneration = -1
        visitedPoints.valueIterator().forEach { point ->
            if (maxGeneration < point.generation) {
                maxGeneration = point.generation
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
        private const val DEFAULT_QUEUE_SIZE = 100
        private const val ALPHA_COLOR_OFFSET = 24
        private const val MAX_ALPHA = 255
        private const val ALPHA_SEGMENTS = 4
        private const val SHUFFLE_STEP = 18
        private const val TRANSPARENCY_ANIMATION_DELAY = 15L
    }
}
