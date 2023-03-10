package ru.tinkoff.mobile.tech.ti_recycler_rx2

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import ru.tinkoff.mobile.tech.ti_recycler.BaseTiRecycler
import ru.tinkoff.mobile.tech.ti_recycler.TiRecyclerBuilder
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler_rx2.base.RxHolderFactory

interface TiRecyclerRx<T : ViewTyped> : BaseTiRecycler<T, RxHolderFactory> {

    companion object {

        @JvmOverloads
        operator fun <T : ViewTyped> invoke(
            recyclerView: RecyclerView,
            holderFactory: RxHolderFactory,
            diffCallback: DiffUtil.ItemCallback<T>? = null,
            init: TiRecyclerBuilder<T, RxHolderFactory, TiRecyclerRx<T>>.() -> Unit = {}
        ): TiRecyclerRx<T> {
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
            adapter: BaseTiAdapter<T, RxHolderFactory>,
            init: TiRecyclerBuilder<T, RxHolderFactory, TiRecyclerRx<T>>.() -> Unit = {}
        ): TiRecyclerRx<T> {
            return TiRecyclerBuilderImpl(adapter = adapter)
                .apply(init)
                .build(recyclerView)
        }
    }

    fun <R : ViewTyped> clickedItem(vararg viewType: Int): Observable<R>
    fun <R : ViewTyped> clickedViewId(viewType: Int, viewId: Int): Observable<R>
    fun <R : ViewTyped> longClickedItem(vararg viewType: Int): Observable<R>
    fun <R : ViewTyped> longClickedViewId(viewType: Int, viewId: Int): Observable<R>
    fun <R : ViewTyped> checkChanged(viewType: Int, viewId: Int): Observable<Pair<R, Boolean>>
    fun <R : ViewTyped> swipeToDismiss(vararg viewType: Int): Observable<R>
}

@Suppress("UNCHECKED_CAST")
internal class TiRecyclerRxImpl<T : ViewTyped>(
    override val recyclerView: RecyclerView,
    override val adapter: BaseTiAdapter<T, RxHolderFactory>
) : TiRecyclerRx<T> {

    override fun setItems(items: List<T>) {
        adapter.items = items
    }

    override fun <R : ViewTyped> clickedItem(vararg viewType: Int): Observable<R> {
        return adapter.holderFactory.clickPosition(*viewType).map { adapter.items[it] as R }
    }

    override fun <R : ViewTyped> clickedViewId(viewType: Int, viewId: Int): Observable<R> {
        return adapter.holderFactory.clickPosition(viewType, viewId).map { adapter.items[it] as R }
    }

    override fun <R : ViewTyped> longClickedItem(vararg viewType: Int): Observable<R> {
        return adapter.holderFactory.longClickPosition(*viewType).map { adapter.items[it] as R }
    }

    override fun <R : ViewTyped> longClickedViewId(viewType: Int, viewId: Int): Observable<R> {
        return adapter.holderFactory.longClickPosition(viewType, viewId)
            .map { adapter.items[it] as R }
    }

    override fun <R : ViewTyped> checkChanged(
        viewType: Int,
        viewId: Int
    ): Observable<Pair<R, Boolean>> {
        return adapter.holderFactory.checkChanges(viewType, viewId)
            .map { (clickedPosition, isChecked) -> adapter.items[clickedPosition] as R to isChecked }
    }

    override fun <R : ViewTyped> swipeToDismiss(vararg viewType: Int): Observable<R> {
        return adapter.holderFactory.swipesToDismiss(*viewType)
            .map { adapter.items[it] as R }
    }
}
