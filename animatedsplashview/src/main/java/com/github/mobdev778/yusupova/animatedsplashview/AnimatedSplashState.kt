package com.github.mobdev778.yusupova.animatedsplashview

import android.graphics.Bitmap
import android.graphics.Color
import com.github.mobdev778.yusupova.animatedsplashview.figure.Point
import com.github.mobdev778.yusupova.animatedsplashview.figure.XYMap
import kotlinx.coroutines.delay

@Suppress("TooManyFunctions")
internal data class AnimatedSplashState(
    val number: Int = 0,
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
        originalBitmap: Bitmap
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
        if (pointsAdded == 0) {
            val alphaValues = updateGenerations()
            return copy(
                number = number + 1,
                alphaValues = alphaValues
            )
        }
        return copy(
            number = number + 1,
        )
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

        var generation: Int? = null

        while (pointsAdded < pointsPerFrame && edgePoints.isNotEmpty()) {
            val point = edgePoints.removeFirst()
            generation = generation ?: point.generation
            pointsAdded++
            bitmapPixels[point.offset] = point.color

            if (pointsAdded % SHUFFLE_STEP == 0) {
                edgePoints.shuffle()
            }

            if (point.y > 0) {
                if (point.x > 0) {
                    addNeighbour(point.x - 1, point.y - 1, generation)
                }
                addNeighbour(point.x, point.y - 1, generation)
                if (point.x < width - 1) {
                    addNeighbour(point.x + 1, point.y - 1, generation)
                }
            }

            if (point.x > 0) {
                addNeighbour(point.x - 1, point.y, generation)
            }
            if (point.x < width - 1) {
                addNeighbour(point.x + 1, point.y, generation)
            }

            if (point.y < height - 1) {
                if (point.x > 0) {
                    addNeighbour(point.x - 1, point.y + 1, generation)
                }
                addNeighbour(point.x, point.y + 1, generation)
                if (point.x < width - 1) {
                    addNeighbour(point.x + 1, point.y + 1, generation)
                }
            }
        }

        bitmap?.setPixels(bitmapPixels, 0, width, 0, 0, width, height)
        return pointsAdded
    }

    private suspend fun updateGenerations(): IntArray {
        val alphaValues = if (alphaValues.isEmpty()) {
            calculateAlphaValues()
        } else {
            this.alphaValues
        }

        delay(TRANSPARENCY_ANIMATION_DELAY)
        visitedPoints.valueIterator().forEach { point ->
            val index = point.generation % alphaValues.size
            val maskedRGB = point.getMaskedRgb()
            val newColor = alphaValues[index].shl(ALPHA_COLOR_OFFSET).or(maskedRGB)
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

    private fun addNeighbour(x: Int, y: Int, generation: Int) {
        val neighbour = visitedPoints.get(x, y)
        if (neighbour == null) {
            val color = originalBitmap?.getPixel(x, y)
            if (color != Color.TRANSPARENT && color != null) {
                val newPoint = Point(x, y, y * width + x, color, generation + 1)
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
        val alphaValues = IntArray(maxGeneration * ALPHA_SEGMENTS)
        for (index in 0 until maxGeneration) {
            alphaValues[index] = MAX_ALPHA
            alphaValues[index + maxGeneration] = MAX_ALPHA - MAX_ALPHA * index / maxGeneration
            alphaValues[index + maxGeneration * LAST_ALPHA_SEGMENT] = MAX_ALPHA * index / maxGeneration
        }
        return alphaValues
    }

    companion object {
        private const val DEFAULT_QUEUE_SIZE = 100
        private const val ALPHA_COLOR_OFFSET = 24
        private const val MAX_ALPHA = 255
        private const val ALPHA_SEGMENTS = 4
        private const val LAST_ALPHA_SEGMENT = ALPHA_SEGMENTS - 1
        private const val SHUFFLE_STEP = 18
        private const val TRANSPARENCY_ANIMATION_DELAY = 15L
    }
}
