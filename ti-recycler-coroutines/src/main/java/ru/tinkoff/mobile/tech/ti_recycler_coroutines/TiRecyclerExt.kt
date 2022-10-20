package ru.tinkoff.mobile.tech.ti_recycler_coroutines

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.tinkoff.mobile.tech.ti_recycler.BaseTiRecycler
import ru.tinkoff.mobile.tech.ti_recycler.TiRecyclerBuilder
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.base.CoroutinesHolderFactory

interface TiRecyclerCoroutines<T : ViewTyped> : BaseTiRecycler<T, CoroutinesHolderFactory> {

    companion object {

        @JvmOverloads
        operator fun <T : ViewTyped> invoke(
            recyclerView: RecyclerView,
            holderFactory: CoroutinesHolderFactory,
            diffCallback: DiffUtil.ItemCallback<T>? = null,
            init: TiRecyclerBuilder<T, CoroutinesHolderFactory, TiRecyclerCoroutines<T>>.() -> Unit = {}
        ): TiRecyclerCoroutines<T> {
            return TiRecyclerBuilderImpl(
                holderFactory = holderFactory,
                diffCallback = diffCallback
            )
                .apply(init)
                .build(recyclerView)
        }

        @JvmOverloads
        operator fun <T : ViewTyped> invoke(
            recyclerView: RecyclerView,
            adapter: BaseTiAdapter<T, CoroutinesHolderFactory>,
            init: TiRecyclerBuilder<T, CoroutinesHolderFactory, TiRecyclerCoroutines<T>>.() -> Unit = {}
        ): TiRecyclerCoroutines<T> {
            return TiRecyclerBuilderImpl(adapter = adapter)
                .apply(init)
                .build(recyclerView)
        }
    }

    fun <R : ViewTyped> clickedItem(vararg viewType: Int): Flow<R>
    fun <R : ViewTyped> clickedViewId(viewType: Int, viewId: Int): Flow<R>
    fun <R : ViewTyped> longClickedItem(vararg viewType: Int): Flow<R>
    fun <R : ViewTyped> longClickedViewId(viewType: Int, viewId: Int): Flow<R>
    fun <R : ViewTyped> checkChanged(viewType: Int, viewId: Int): Flow<Pair<R, Boolean>>
}

@Suppress("UNCHECKED_CAST")
internal class TiRecyclerCoroutinesImpl<T : ViewTyped>(
    override val recyclerView: RecyclerView,
    override val adapter: BaseTiAdapter<T, CoroutinesHolderFactory>
) : TiRecyclerCoroutines<T> {

    override fun setItems(items: List<T>) {
        adapter.items = items
    }

    override fun <R : ViewTyped> clickedItem(vararg viewType: Int): Flow<R> {
        return adapter.holderFactory.clickPosition(*viewType).map { adapter.items[it] as R }
    }

    override fun <R : ViewTyped> clickedViewId(viewType: Int, viewId: Int): Flow<R> {
        return adapter.holderFactory.clickPosition(viewType, viewId).map { adapter.items[it] as R }
    }

    override fun <R : ViewTyped> longClickedItem(vararg viewType: Int): Flow<R> {
        return adapter.holderFactory.longClickPosition(*viewType).map { adapter.items[it] as R }
    }

    override fun <R : ViewTyped> longClickedViewId(viewType: Int, viewId: Int): Flow<R> {
        return adapter.holderFactory.longClickPosition(viewType, viewId)
            .map { adapter.items[it] as R }
    }

    override fun <R : ViewTyped> checkChanged(
        viewType: Int,
        viewId: Int
    ): Flow<Pair<R, Boolean>> {
        return adapter.holderFactory.checkChanges(viewType, viewId)
            .map { (clickedPosition, isChecked) -> adapter.items[clickedPosition] as R to isChecked }
    }
}
