package com.github.mobdev778.yusupova.animatedsplashview.figure

internal class XYMap<T> {

    private val xyValueMap = mutableMapOf<Int, T>()

    fun get(x: Int, y: Int): T? {
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

    fun keyIterator(): Iterator<Pair<Int, Int>> {
        return XYMapIterator(xyValueMap.keys.iterator())
    }

    fun valueIterator(): Iterator<T> {
        return xyValueMap.values.iterator()
    }

    private class XYMapIterator(private val iterator: Iterator<Int>): Iterator<Pair<Int, Int>> {

        override fun hasNext(): Boolean {
            return iterator.hasNext()
        }

        override fun next(): Pair<Int, Int> {
            val next = iterator.next()
            val x = next.shr(X_BIT_SHIFT).and(0xFFF)
            val y = next.and(0xFFF)
            return Pair(x, y)
        }
    }

    companion object {
        private const val X_BIT_SHIFT = 18
        private const val MAX_X_VALUE = (1.shl(X_BIT_SHIFT)) - 1
    }
}
