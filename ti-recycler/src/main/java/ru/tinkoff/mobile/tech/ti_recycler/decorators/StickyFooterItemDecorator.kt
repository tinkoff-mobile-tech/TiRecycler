package ru.tinkoff.mobile.tech.ti_recycler.decorators

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.baseAdapter

class StickyFooterItemDecoration(private val isFooter: (ViewTyped) -> Boolean) : RecyclerView.ItemDecoration() {

    companion object {

        const val DIRECTION_TOP = 1
        const val DIRECTION_BOTTOM = -1
    }

    private var footerViewHolder: BaseViewHolder<ViewTyped>? = null

    override fun onDrawOver(canvas: Canvas, recyclerView: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, recyclerView, state)
        with(recyclerView) {
            //There are not enough elements in the list to display the footer
            if (canScrollVerticallyInAnyDirections().not()) return

            //There is no footer in the list
            val footerItem = getFooterItem() ?: return

            //The footer is already displayed in the list
            if (isFooterViewVisible()) return

            val footerView = getFooterView(footerItem)

            addFooterView(footerView)
            drawFooterView(footerView, canvas)
        }
    }

    private fun RecyclerView.canScrollVerticallyInAnyDirections(): Boolean {
        return canScrollVertically(DIRECTION_TOP) || canScrollVertically(DIRECTION_BOTTOM)
    }

    private fun RecyclerView.getFooterItem(): ViewTyped? {
        return baseAdapter.items.find(isFooter)
    }

    private fun RecyclerView.isFooterViewVisible(): Boolean {
        return children.any { child ->
            getChildAdapterPosition(child)
                .takeIf { it != RecyclerView.NO_POSITION }
                ?.let { isFooter(baseAdapter.items[it]) }
                ?: false
        }
    }

    private fun RecyclerView.getFooterView(footerItem: ViewTyped): View {
        var viewHolder = footerViewHolder
        if (viewHolder == null) {
            viewHolder = baseAdapter.holderFactory(this, footerItem.viewType)
            footerViewHolder = viewHolder
        }

        viewHolder.bind(footerItem)
        return viewHolder.itemView
    }

    private fun RecyclerView.addFooterView(footerView: View) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.UNSPECIFIED)

        val childWidthSpec = ViewGroup.getChildMeasureSpec(
            widthSpec,
            paddingLeft + paddingRight,
            footerView.layoutParams.width
        )
        val childHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            paddingTop + paddingBottom,
            footerView.layoutParams.height
        )
        footerView.measure(childWidthSpec, childHeightSpec)
        footerView.layout(0, 0, footerView.measuredWidth, footerView.measuredHeight)
    }

    private fun RecyclerView.drawFooterView(footerView: View, canvas: Canvas) {
        canvas.save()
        canvas.translate(0F, height - footerView.height.toFloat())
        footerView.draw(canvas)
        canvas.restore()
    }
}
