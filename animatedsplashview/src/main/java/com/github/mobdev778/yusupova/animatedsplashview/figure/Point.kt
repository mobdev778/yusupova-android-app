package com.github.mobdev778.yusupova.animatedsplashview.figure

data class Point(
    val x: Int,
    val y: Int,
    val offset: Int,
    val color: Int,
    val generation: Int
) {
    var maskedRgb: Int? = null

    fun getMaskedRgb(): Int {
        val cachedRgb = maskedRgb
        if (cachedRgb != null) {
            return cachedRgb
        }
        val newCachedRgb = color.and(RGB_MASK)
        maskedRgb = newCachedRgb
        return newCachedRgb
    }

    companion object {
        const val RGB_MASK = 0xFFFFFF
    }
}
