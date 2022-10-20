package ru.tinkoff.mobile.tech.ti_recycler_coroutines

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.TiRecyclerBuilder
import ru.tinkoff.mobile.tech.ti_recycler.adapters.AsyncTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.adapters.SimpleTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.base.CoroutinesHolderFactory

internal class TiRecyclerBuilderImpl<T : ViewTyped>(
    override val adapter: BaseTiAdapter<T, CoroutinesHolderFactory>
) : TiRecyclerBuilder<T, CoroutinesHolderFactory, TiRecyclerCoroutines<T>> {

    constructor(
        holderFactory: CoroutinesHolderFactory,
        diffCallback: DiffUtil.ItemCallback<T>? = null
    ) : this(
        diffCallback
            ?.run { AsyncTiAdapter(holderFactory, this) }
            ?: SimpleTiAdapter(holderFactory)
    )

    override val itemDecoration: MutableList<RecyclerView.ItemDecoration> = mutableListOf()
    override var layoutManager: RecyclerView.LayoutManager? = null
    override var hasFixedSize: Boolean = true

    override fun build(recyclerView: RecyclerView): TiRecyclerCoroutines<T> {
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            layoutManager ?: recyclerView.layoutManager ?: LinearLayoutManager(recyclerView.context)

        itemDecoration.forEach(recyclerView::addItemDecoration)
        recyclerView.setHasFixedSize(hasFixedSize)
        return TiRecyclerCoroutinesImpl(recyclerView, adapter)
    }
}
