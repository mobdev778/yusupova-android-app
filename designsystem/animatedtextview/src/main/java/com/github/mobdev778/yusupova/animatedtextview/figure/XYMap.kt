package com.github.mobdev778.yusupova.animatedtextview.figure

internal class XYMap<T> {

    private val xyValueMap = mutableMapOf<Int, T>()

    fun get(x: Int, y: Int): T? {
        if (x < 0 || y < 0) {
            return null
        }
        val key = x.shl(X_BIT_SHIFT).or(y)
        return xyValueMap[key]
    }

    fun put(x: Int, y: Int, value: T) {
        require(x < MAX_X_VALUE) { "X value exceeds maximum: $MAX_X_VALUE" }
        val key = x.shl(X_BIT_SHIFT).or(y)
        xyValueMap[key] = value
    }

    fun size(): Int {
        return xyValueMap.size
    }

    fun valueIterator(): Iterator<T> {
        return xyValueMap.values.iterator()
    }

    companion object {
        private const val X_BIT_SHIFT = 18
        private const val MAX_X_VALUE = (1.shl(X_BIT_SHIFT)) - 1
    }
}
