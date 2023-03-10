package ru.tinkoff.mobile.tech.ti_recycler.swipes

import android.graphics.Rect

internal class RectEvaluator(private val reuseRect: Rect) {

    operator fun invoke(fraction: Float, startValue: Rect, endValue: Rect) {
        val left = startValue.left + ((endValue.left - startValue.left) * fraction).toInt()
        val top = startValue.top + ((endValue.top - startValue.top) * fraction).toInt()
        val right = startValue.right + ((endValue.right - startValue.right) * fraction).toInt()
        val bottom = startValue.bottom + ((endValue.bottom - startValue.bottom) * fraction).toInt()
        reuseRect.set(left, top, right, bottom)
    }
}
