package ru.tinkoff.mobile.tech.ti_recycler

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.HolderFactory
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.base.footer.StickyFooterViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.base.header.StickyHeaderViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.decorators.StickyFooterItemDecoration
import ru.tinkoff.mobile.tech.ti_recycler.decorators.StickyHeaderItemDecoration
import ru.tinkoff.mobile.tech.ti_recycler.swipes.ItemDismissTouchHelperCallback

abstract class TiRecyclerBuilder<VT : ViewTyped, HF : HolderFactory, TiRecycler : BaseTiRecycler<VT, HF>>(
    val adapter: BaseTiAdapter<VT, HF>
) {

    private val itemDecoration: MutableList<RecyclerView.ItemDecoration> = mutableListOf()
    var layoutManager: RecyclerView.LayoutManager? = null
    var hasFixedSize: Boolean = true
    val itemDismissCallbacks: MutableList<ItemDismissTouchHelperCallback> = mutableListOf()

    fun withStickyHeaders(isHeader: (ViewTyped) -> Boolean = { it is StickyHeaderViewTyped }) {
        itemDecoration += StickyHeaderItemDecoration(isHeader)
    }

    fun withStickyFooters(isFooter: (ViewTyped) -> Boolean = { it is StickyFooterViewTyped }) {
        itemDecoration += StickyFooterItemDecoration(isFooter)
    }

    protected fun setup(recyclerView: RecyclerView) {
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            layoutManager ?: recyclerView.layoutManager ?: LinearLayoutManager(recyclerView.context)
        itemDecoration.forEach(recyclerView::addItemDecoration)
        itemDismissCallbacks.forEach { itemDismissCallback ->
            ItemTouchHelper(itemDismissCallback).attachToRecyclerView(recyclerView)
        }
        recyclerView.setHasFixedSize(hasFixedSize)
    }
    abstract fun build(recyclerView: RecyclerView): TiRecycler
}
