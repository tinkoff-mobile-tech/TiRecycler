package ru.tinkoff.mobile.tech.ti_recycler

import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.HolderFactory
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.base.header.StickyHeaderViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.decorators.StickyHeaderItemDecoration

interface TiRecyclerBuilder<T : ViewTyped, HF : HolderFactory, TiRecycler : BaseTiRecycler<T, HF>> {
    val itemDecoration: MutableList<RecyclerView.ItemDecoration>
    val adapter: BaseTiAdapter<T, HF>
    var layoutManager: RecyclerView.LayoutManager?
    var hasFixedSize: Boolean

    fun withStickyHeaders(isHeader: (ViewTyped) -> Boolean = { it is StickyHeaderViewTyped }) {
        itemDecoration += StickyHeaderItemDecoration(isHeader)
    }

    fun build(recyclerView: RecyclerView): TiRecycler
}

