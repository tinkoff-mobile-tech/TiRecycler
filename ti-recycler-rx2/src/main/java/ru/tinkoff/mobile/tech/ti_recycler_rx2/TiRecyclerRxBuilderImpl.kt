package ru.tinkoff.mobile.tech.ti_recycler_rx2

import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.TiRecyclerBuilder
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler_rx2.base.RxHolderFactory

internal class TiRecyclerRxBuilderImpl<VT : ViewTyped>(
    adapter: BaseTiAdapter<VT, RxHolderFactory>
) : TiRecyclerBuilder<VT, RxHolderFactory, TiRecyclerRx<VT>>(adapter) {

    override fun build(recyclerView: RecyclerView): TiRecyclerRx<VT> {
        setup(recyclerView)
        itemDismissCallbacks.forEach { itemDismissCallback ->
            adapter.holderFactory.swipesToDismiss.addOnDismissListener(itemDismissCallback)
        }
        return TiRecyclerRxImpl(adapter)
    }
}
