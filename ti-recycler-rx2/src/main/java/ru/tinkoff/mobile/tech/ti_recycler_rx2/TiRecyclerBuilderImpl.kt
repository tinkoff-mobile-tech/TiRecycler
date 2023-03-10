package ru.tinkoff.mobile.tech.ti_recycler_rx2

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.TiRecyclerBuilder
import ru.tinkoff.mobile.tech.ti_recycler.adapters.AsyncTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.adapters.SimpleTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.swipes.ItemDismissTouchHelperCallback
import ru.tinkoff.mobile.tech.ti_recycler_rx2.base.RxHolderFactory

internal class TiRecyclerBuilderImpl<T : ViewTyped>(
    override val adapter: BaseTiAdapter<T, RxHolderFactory>
) : TiRecyclerBuilder<T, RxHolderFactory, TiRecyclerRx<T>> {

    constructor(
        holderFactory: RxHolderFactory,
        diffCallback: DiffUtil.ItemCallback<T>? = null
    ) : this(diffCallback?.run {
        AsyncTiAdapter(holderFactory, this)
    } ?: SimpleTiAdapter(holderFactory))

    override val itemDecoration: MutableList<RecyclerView.ItemDecoration> = mutableListOf()
    override var layoutManager: RecyclerView.LayoutManager? = null
    override var hasFixedSize: Boolean = true
    override val itemDismissCallbacks: MutableList<ItemDismissTouchHelperCallback> = mutableListOf()

    override fun build(recyclerView: RecyclerView): TiRecyclerRx<T> {
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            layoutManager ?: recyclerView.layoutManager ?: LinearLayoutManager(recyclerView.context)

        itemDecoration.forEach(recyclerView::addItemDecoration)
        itemDismissCallbacks.forEach { itemDismissCallback ->
            ItemTouchHelper(itemDismissCallback).attachToRecyclerView(recyclerView)
            adapter.holderFactory.swipesToDismiss.addOnDismissListener(itemDismissCallback)
        }

        recyclerView.setHasFixedSize(hasFixedSize)
        return TiRecyclerRxImpl(recyclerView, adapter)
    }
}
