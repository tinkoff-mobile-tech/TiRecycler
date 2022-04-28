package ru.tinkoff.mobile.tech.ti_recycler.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.dpToPx

class ItemSimpleMarginsDecoration(
    private val viewTypes: IntArray,
    private val left: Int = 0,
    private val top: Int = 0,
    private val right: Int = 0,
    private val bottom: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val viewType = parent.getChildViewHolder(view).itemViewType
        if (viewType in viewTypes) {
            outRect.set(view.dpToPx(left), view.dpToPx(top), view.dpToPx(right), view.dpToPx(bottom))
        }
    }
}
