package ru.tinkoff.mobile.tech.ti_recycler.decorators

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.baseAdapter

/**
 * Based on https://github.com/smhdk/KM-Recyclerview-Sticky-Header/blob/master/kmrecyclerviewstickyheader/src/main/java/com/kodmap/library/kmrecyclerviewstickyheader/KmHeaderItemDecoration.java
 */
class StickyHeaderItemDecoration(private val isHeader: (ViewTyped) -> Boolean) :
    RecyclerView.ItemDecoration() {

    private val headerViewHolders = mutableMapOf<ViewTyped, BaseViewHolder<ViewTyped>>()
    private var headerHeight: Int? = null

    @Suppress("ReturnCount")
    override fun onDrawOver(c: Canvas, recyclerView: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, recyclerView, state)
        val topChild: View = recyclerView.getChildAt(0) ?: return

        val topChildPosition: Int = recyclerView.getChildAdapterPosition(topChild)
        val isTopChildNotLessTopParent =
            !recyclerView.clipToPadding && recyclerView.top < topChild.top
        if (topChildPosition == RecyclerView.NO_POSITION || isTopChildNotLessTopParent) return

        val headerPos: Int = getHeaderPositionForItem(recyclerView, topChild)
        if (headerPos == RecyclerView.NO_POSITION) return

        val currentHeader = getHeaderViewForItem(headerPos, recyclerView)
        fixLayoutSize(recyclerView, currentHeader)
        val contactPoint = currentHeader.bottom
        val childInContact = getChildInContact(recyclerView, contactPoint)
        if (childInContact != null) {
            moveHeader(c, currentHeader, childInContact)
            return
        }
        drawHeader(c, currentHeader)
    }

    private fun getHeaderViewForItem(headerPosition: Int, recyclerView: RecyclerView): View {
        val adapter = recyclerView.baseAdapter
        val item = adapter.items[headerPosition]
        var holder = headerViewHolders[item]
        if (holder == null) {
            holder = adapter.holderFactory(recyclerView, item.viewType)
            headerViewHolders[item] = holder
        }
        holder.bind(item)
        return holder.itemView
    }

    private fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0f, nextHeader.top - currentHeader.height.toFloat())
        currentHeader.draw(c)
        c.restore()
    }

    private fun getChildInContact(recyclerView: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until recyclerView.childCount) {
            var heightDiff = 0
            val child: View = recyclerView.getChildAt(i)
            //measure height diff with child if child is another header
            val isChildHeader: Boolean = isHeader(recyclerView, child)
            if (isChildHeader) {
                heightDiff = headerHeight!! - child.height
            }

            //add heightDiff if child top be in display area(just below header)
            val childBottomPosition = if (child.top > 0) child.bottom + heightDiff else child.bottom
            if (childBottomPosition > contactPoint && child.top <= contactPoint) {
                if (isChildHeader) childInContact = child
                break
            }
        }
        return childInContact
    }

    /**
     * Properly measures and layouts the top sticky header.
     *
     * @param parent ViewGroup: RecyclerView in this case.
     */
    private fun fixLayoutSize(parent: ViewGroup, view: View) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        val childWidthSpec = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )
        view.measure(childWidthSpec, childHeightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight.also { headerHeight = it })
    }

    private fun isHeader(recyclerView: RecyclerView, view: View): Boolean {
        val position = recyclerView.getChildAdapterPosition(view)
        return isHeader(recyclerView.baseAdapter.items[position])
    }

    private fun getHeaderPositionForItem(recyclerView: RecyclerView, view: View): Int {
        val position = recyclerView.getChildAdapterPosition(view)
        return recyclerView.baseAdapter.items.asSequence().take(position + 1).indexOfLast(isHeader)
    }
}
