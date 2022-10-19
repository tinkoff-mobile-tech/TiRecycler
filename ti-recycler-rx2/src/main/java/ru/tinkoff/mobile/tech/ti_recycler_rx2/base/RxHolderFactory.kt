package ru.tinkoff.mobile.tech.ti_recycler_rx2.base

import io.reactivex.Observable
import ru.tinkoff.mobile.tech.ti_recycler.base.HolderFactory
import ru.tinkoff.mobile.tech.ti_recycler.clicks.ItemClick
import ru.tinkoff.mobile.tech.ti_recycler_rx2.clicks.TiRecyclerCheckChangeObservable
import ru.tinkoff.mobile.tech.ti_recycler_rx2.clicks.TiRecyclerItemClicksObservable
import ru.tinkoff.mobile.tech.ti_recycler_rx2.clicks.TiRecyclerItemLongClicksObservable

abstract class RxHolderFactory : HolderFactory {

    protected val clicks = TiRecyclerItemClicksObservable()
    protected val longClicks = TiRecyclerItemLongClicksObservable()
    protected val checkChanges = TiRecyclerCheckChangeObservable()

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
}
