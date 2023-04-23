package com.github.mobdev778.yusupova.designsystem.animatedsplashview.figure

data class Point(
    val x: Int,
    val y: Int,
    val offset: Int,
    val color: Int,
    var generation: Int = 0
) {
    private val maskedRgb: Int = color.and(RGB_MASK)
    private val alpha: Int = color.shr(ALPHA_OFFSET).and(ALPHA_MASK)

    fun getColor(alpha: Int): Int {
        if (alpha > this.alpha) {
            return color
        }
        return alpha.shl(ALPHA_OFFSET).or(maskedRgb)
    }

    companion object {
        private const val RGB_MASK = 0xFFFFFF
        private const val ALPHA_OFFSET = 24
        private const val ALPHA_MASK = 0xFF
    }
}
