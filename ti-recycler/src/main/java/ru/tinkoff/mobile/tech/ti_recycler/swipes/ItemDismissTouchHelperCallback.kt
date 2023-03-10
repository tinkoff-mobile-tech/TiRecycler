package ru.tinkoff.mobile.tech.ti_recycler.swipes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.dpToPx

private const val FRACTION_FACTOR = 200

open class ItemDismissTouchHelperCallback(
    context: Context,
    private val dismissibleViewType: Int,
    private val isSwipeToDeleteEnabled: () -> Boolean = { true },
    private val icon: Drawable,
    private val dismissBackgroundColor: Int,
    private val normalBackgroundColor: Int,
    private val swipeDirections: Int = ItemTouchHelper.START or ItemTouchHelper.END,
    private val elevation: Float,
    private val cornerRadius: Float,
    drawableOffsetInDp: Int,
) : ItemTouchHelper.Callback() {

    private val drawableOffset = context.dpToPx(drawableOffsetInDp)
    private var onItemDismissListeners: MutableList<OnItemDismissListener> = mutableListOf()
    private val minBounds = Rect()
    private val maxBounds = Rect()
    private val evaluatedBounds = Rect()
    private val backgroundRect = Rect()
    private val boundsEvaluator = RectEvaluator(evaluatedBounds)
    private val paint = Paint()
    private val roundedBackground = GradientDrawable().apply {
        setColor(normalBackgroundColor)
        cornerRadius = this@ItemDismissTouchHelperCallback.cornerRadius
    }
    private val normalBackground: Drawable = ColorDrawable(normalBackgroundColor)

    fun addOnItemDismissListener(listener: OnItemDismissListener) = onItemDismissListeners.add(listener)

    fun clearListeners() = onItemDismissListeners.clear()

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun isItemViewSwipeEnabled(): Boolean = true

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val movementFlags =
            if (viewHolder.itemViewType == dismissibleViewType && isSwipeToDeleteEnabled()) swipeDirections else 0
        return makeMovementFlags(0, movementFlags)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onItemDismissListeners.forEach { it.onItemDismiss(viewHolder) }
    }

    override fun onChildDraw(
        canvas: Canvas,
        recycler: RecyclerView,
        holder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        state: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(canvas, recycler, holder, dX, dY, state, isCurrentlyActive)
        when {
            !isCurrentlyActive && dX == 0f -> {
                drawRect(canvas, normalBackgroundColor, holder)
                ViewCompat.setElevation(holder.itemView, 0f)
                holder.itemView.background = normalBackground
            }
            state == ItemTouchHelper.ACTION_STATE_SWIPE -> {
                drawRect(canvas, dismissBackgroundColor, holder)
                calculateIconBounds(dX, holder.itemView)
                if (evaluatedBounds.top <= maxBounds.top) icon.bounds = maxBounds else icon.bounds = evaluatedBounds
                icon.draw(canvas)
                ViewCompat.setElevation(holder.itemView, elevation)
                holder.itemView.background = roundedBackground
            }
        }
    }

    private fun drawRect(canvas: Canvas, paintColor: Int, holder: RecyclerView.ViewHolder) {
        val itemView = holder.itemView
        backgroundRect.set(itemView.left, itemView.top, itemView.right, itemView.bottom)
        canvas.drawRect(backgroundRect, paint.apply { color = paintColor })
    }

    private fun calculateIconBounds(dX: Float, childView: View) {
        val fraction = dX / FRACTION_FACTOR
        val childCenter = childView.height / 2
        val topBottomOffset = childCenter - icon.intrinsicHeight / 2
        val childViewTop = childView.top
        val childViewBottom = childView.bottom
        minBounds.top = childViewTop + childCenter
        minBounds.bottom = childViewBottom - childCenter
        maxBounds.top = childViewTop + topBottomOffset
        maxBounds.bottom = childViewBottom - topBottomOffset
        if (dX > 0) {
            calculateLeftIconBounds(childView.left, fraction)
        } else {
            calculateRightIconBounds(childView.right, fraction)
        }
    }

    private fun calculateRightIconBounds(viewPositionRight: Int, fraction: Float) {
        val iconIntrinsicWidth = icon.intrinsicWidth
        minBounds.left = viewPositionRight - drawableOffset - iconIntrinsicWidth / 2
        minBounds.right = viewPositionRight - drawableOffset - iconIntrinsicWidth / 2
        maxBounds.left = viewPositionRight - iconIntrinsicWidth - drawableOffset
        maxBounds.right = viewPositionRight - drawableOffset
        boundsEvaluator(-fraction, minBounds, maxBounds)
    }

    private fun calculateLeftIconBounds(viewPositionLeft: Int, fraction: Float) {
        val iconIntrinsicWidth = icon.intrinsicWidth
        minBounds.left = viewPositionLeft + drawableOffset + iconIntrinsicWidth / 2
        minBounds.right = viewPositionLeft + drawableOffset + iconIntrinsicWidth / 2
        maxBounds.left = viewPositionLeft + drawableOffset
        maxBounds.right = viewPositionLeft + iconIntrinsicWidth + drawableOffset
        boundsEvaluator(fraction, minBounds, maxBounds)
    }
}
