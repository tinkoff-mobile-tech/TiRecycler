package ru.tinkoff.mobile.tech.ti_recycler.decorators

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import kotlin.math.roundToInt

/**
 * Based on
 * @see androidx.recyclerview.widget.DividerItemDecoration
 */
class DividerItemDecoration(
    context: Context,
    private val leftPadding: Int = 0,
    private val rightPadding: Int = 0,
    private vararg var dividedViewTypes: Int = IntArray(0),
    divider: Drawable? = null,
    private val orientation: Int = VERTICAL,
    private val needDividerForLastElement: Boolean = true
) : RecyclerView.ItemDecoration() {

    private val divider: Drawable
    private val bounds = Rect()

    init {
        val attrs = intArrayOf(android.R.attr.listDivider)
        val a = context.obtainStyledAttributes(attrs)
        this.divider = divider ?: a.getDrawable(0)!!
        a.recycle()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        when (orientation) {
            VERTICAL -> drawVertical(c, parent)
            else -> drawHorizontal(c, parent)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int

        if (parent.clipToPadding) {
            left = parent.paddingLeft + leftPadding
            right = parent.width - parent.paddingRight - rightPadding
            canvas.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
        } else {
            left = leftPadding
            right = parent.width - rightPadding
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            if (shouldDrawDivider(i, child, parent)) {
                parent.getDecoratedBoundsWithMargins(child, bounds)
                val bottom = bounds.bottom + child.translationY.roundToInt()
                val top = bottom - divider.intrinsicHeight
                divider.setBounds(left, top, right, bottom)
                divider.draw(canvas)
            }
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top: Int
        val bottom: Int

        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(parent.paddingLeft, top, parent.width - parent.paddingRight, bottom)
        } else {
            top = 0
            bottom = parent.height
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.layoutManager?.getDecoratedBoundsWithMargins(child, bounds)
            val right = bounds.right + child.translationX.roundToInt()
            val left = right - divider.intrinsicWidth
            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when (orientation) {
            VERTICAL -> outRect.set(0, 0, 0, divider.intrinsicHeight)
            else -> outRect.set(0, 0, divider.intrinsicWidth, 0)
        }
    }

    private fun shouldDrawDivider(position: Int, view: View, parent: RecyclerView): Boolean {
        val holder = parent.getChildViewHolder(view)
        val typeCondition = dividedViewTypes.isEmpty() || holder.itemViewType in dividedViewTypes
        val positionCondition = needDividerForLastElement || position != parent.childCount - 1
        return typeCondition && positionCondition
    }
}
