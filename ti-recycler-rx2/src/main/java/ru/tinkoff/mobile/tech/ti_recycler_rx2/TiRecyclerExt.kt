package ru.tinkoff.mobile.tech.ti_recycler_rx2

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import ru.tinkoff.mobile.tech.ti_recycler.BaseTiRecycler
import ru.tinkoff.mobile.tech.ti_recycler.TiRecyclerBuilder
import ru.tinkoff.mobile.tech.ti_recycler.adapters.AsyncTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.adapters.SimpleTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.HolderFactory
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.produceAdapter
import ru.tinkoff.mobile.tech.ti_recycler_rx2.base.RxHolderFactory
import kotlin.reflect.KClass


interface TiRecyclerRx<VT : ViewTyped> : BaseTiRecycler<VT, RxHolderFactory> {

    companion object {

        @JvmOverloads
        operator fun <T : ViewTyped> invoke(
            recyclerView: RecyclerView,
            holderFactory: RxHolderFactory,
            diffCallback: DiffUtil.ItemCallback<T>? = null,
            init: TiRecyclerBuilder<T, RxHolderFactory, TiRecyclerRx<T>>.() -> Unit
        ): TiRecyclerRx<T> {
            val adapter = produceAdapter(holderFactory, diffCallback)
            return TiRecyclerRx(
                recyclerView = recyclerView,
                adapter = adapter,
                init = init
            )
        }

        operator fun <VT : ViewTyped> invoke(
            recyclerView: RecyclerView,
            adapter: BaseTiAdapter<VT, RxHolderFactory>,
            init: TiRecyclerBuilder<VT, RxHolderFactory, TiRecyclerRx<VT>>.() -> Unit
        ): TiRecyclerRx<VT> {
            return TiRecyclerRxBuilderImpl(adapter = adapter)
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
    fun <R : ViewTyped, A : Any> customAction(
        viewType: Int,
        actionClass: KClass<A>
    ): Observable<Pair<R, A>>
}

@Suppress("UNCHECKED_CAST")
internal class TiRecyclerRxImpl<VT : ViewTyped>(
    override val adapter: BaseTiAdapter<VT, RxHolderFactory>
) : TiRecyclerRx<VT> {

    override fun setItems(items: List<VT>) {
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

    override fun <R : ViewTyped, A : Any> customAction(
        viewType: Int,
        actionClass: KClass<A>,
    ): Observable<Pair<R, A>> {
        return adapter.holderFactory.customAction(viewType, actionClass)
            .map { (position, value) -> adapter.items[position] as R to value }
    }
}

inline fun <R : ViewTyped, reified A : Any> TiRecyclerRx<ViewTyped>.customAction(
    viewType: Int
): Observable<Pair<R, A>> {
    return customAction(viewType, A::class)
}
