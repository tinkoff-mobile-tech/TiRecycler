package ru.tinkoff.mobile.tech.ti_recycler_rx2.base

import io.reactivex.Observable
import ru.tinkoff.mobile.tech.ti_recycler.base.HolderFactory
import ru.tinkoff.mobile.tech.ti_recycler.clicks.ItemClick
import ru.tinkoff.mobile.tech.ti_recycler_rx2.actions.TiRecyclerCustomActionObservable
import ru.tinkoff.mobile.tech.ti_recycler_rx2.clicks.TiRecyclerCheckChangeObservable
import ru.tinkoff.mobile.tech.ti_recycler_rx2.clicks.TiRecyclerCheckChangeObservableImpl
import ru.tinkoff.mobile.tech.ti_recycler_rx2.clicks.TiRecyclerItemClicksObservable
import ru.tinkoff.mobile.tech.ti_recycler_rx2.clicks.TiRecyclerItemClicksObservableImpl
import ru.tinkoff.mobile.tech.ti_recycler_rx2.clicks.TiRecyclerItemLongClicksObservable
import ru.tinkoff.mobile.tech.ti_recycler_rx2.clicks.TiRecyclerItemLongClicksObservableImpl
import ru.tinkoff.mobile.tech.ti_recycler_rx2.swipes.OnItemDismissObservable
import ru.tinkoff.mobile.tech.ti_recycler_rx2.swipes.OnItemDismissObservableImpl
import kotlin.reflect.KClass

abstract class RxHolderFactory : HolderFactory {

    internal val swipesToDismiss: OnItemDismissObservable = OnItemDismissObservableImpl()
    protected open val clicks: TiRecyclerItemClicksObservable = TiRecyclerItemClicksObservableImpl()
    protected open val longClicks: TiRecyclerItemLongClicksObservable = TiRecyclerItemLongClicksObservableImpl()
    protected open val checkChanges: TiRecyclerCheckChangeObservable = TiRecyclerCheckChangeObservableImpl()
    protected open val customActions: Map<KClass<*>, TiRecyclerCustomActionObservable<*, *>> = emptyMap()

    fun clickPosition(vararg viewType: Int): Observable<Int> {
        return clicks.filter { it.viewType in viewType }.map(ItemClick::position)
    }

    fun clickPosition(viewType: Int, viewId: Int): Observable<Int> {
        return clicks.filter { it.viewType == viewType && it.view.id == viewId }
            .map(ItemClick::position)
    }

    fun longClickPosition(vararg viewType: Int): Observable<Int> {
        return longClicks.filter { it.viewType in viewType }.map(ItemClick::position)
    }

    fun longClickPosition(viewType: Int, viewId: Int): Observable<Int> {
        return longClicks.filter { it.viewType == viewType && it.view.id == viewId }
            .map(ItemClick::position)
    }

    fun checkChanges(viewType: Int, viewId: Int): Observable<Pair<Int, Boolean>> {
        return checkChanges.filter {
            it.viewType == viewType && it.compoundView.id == viewId
        }.map { it.position to it.compoundView.isChecked }
    }

    fun swipesToDismiss(vararg viewType: Int): Observable<Int> {
        return swipesToDismiss.filter { it.itemViewType in viewType }
            .map { it.absoluteAdapterPosition }
    }

    fun <T : Any> customAction(viewType: Int, actionClass: KClass<T>): Observable<Pair<Int, T>> {
        return customActions.getValue(actionClass).actionsRelay
            .filter { it.viewType == viewType }
            .map { it.position to it.value as T }
    }
}
