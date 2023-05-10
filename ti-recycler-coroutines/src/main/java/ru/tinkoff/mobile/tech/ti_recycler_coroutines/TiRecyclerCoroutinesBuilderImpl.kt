package ru.tinkoff.mobile.tech.ti_recycler_coroutines

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.TiRecyclerBuilder
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.base.CoroutinesHolderFactory

internal class TiRecyclerCoroutinesBuilderImpl<VT : ViewTyped>(
    adapter: BaseTiAdapter<VT, CoroutinesHolderFactory>
) : TiRecyclerBuilder<VT, CoroutinesHolderFactory, TiRecyclerCoroutines<VT>>(adapter) {

    override fun build(recyclerView: RecyclerView): TiRecyclerCoroutines<VT> {
        setup(recyclerView)
        itemDismissCallbacks.forEach { itemDismissCallback ->
            adapter.holderFactory.swipesToDismiss.addOnDismissListener(itemDismissCallback)
        }
        return TiRecyclerCoroutinesImpl(adapter)
    }
}
