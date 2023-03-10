package ru.tinkoff.mobile.tech.ti_recycler

import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.HolderFactory
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.base.footer.StickyFooterViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.base.header.StickyHeaderViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.decorators.StickyFooterItemDecoration
import ru.tinkoff.mobile.tech.ti_recycler.decorators.StickyHeaderItemDecoration
import ru.tinkoff.mobile.tech.ti_recycler.swipes.ItemDismissTouchHelperCallback

interface TiRecyclerBuilder<T : ViewTyped, HF : HolderFactory, TiRecycler : BaseTiRecycler<T, HF>> {
    val itemDecoration: MutableList<RecyclerView.ItemDecoration>
    val adapter: BaseTiAdapter<T, HF>
    var layoutManager: RecyclerView.LayoutManager?
    var hasFixedSize: Boolean
    val itemDismissCallbacks: MutableList<ItemDismissTouchHelperCallback>

    fun withStickyHeaders(isHeader: (ViewTyped) -> Boolean = { it is StickyHeaderViewTyped }) {
        itemDecoration += StickyHeaderItemDecoration(isHeader)
    }

    fun withStickyFooters(isFooter: (ViewTyped) -> Boolean = { it is StickyFooterViewTyped }) {
        itemDecoration += StickyFooterItemDecoration(isFooter)
    }

    fun build(recyclerView: RecyclerView): TiRecycler
}
