package ru.tinkoff.mobile.tech.ti_recycler_coroutines.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import ru.tinkoff.mobile.tech.ti_recycler.base.HolderFactory
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.clicks.TiRecyclerCheckChangeFlow
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.clicks.TiRecyclerItemClicksFlow
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.clicks.TiRecyclerItemLongClicksFlow

abstract class CoroutinesHolderFactory : HolderFactory {

    protected val clicks = TiRecyclerItemClicksFlow()
    protected val longClicks = TiRecyclerItemLongClicksFlow()
    protected val checkChanges = TiRecyclerCheckChangeFlow()

    fun clickPosition(vararg viewType: Int): Flow<Int> {
        return clicks.filter { it.viewType in viewType }
            .map { it.position }
    }

    fun clickPosition(viewType: Int, viewId: Int): Flow<Int> {
        return clicks
            .filter { it.viewType == viewType && it.view.id == viewId }
            .map { it.position }
    }

    fun longClickPosition(vararg viewType: Int): Flow<Int> {
        return longClicks
            .filter { it.viewType in viewType }
            .map { it.position }
    }

    fun longClickPosition(viewType: Int, viewId: Int): Flow<Int> {
        return longClicks
            .filter { it.viewType == viewType && it.view.id == viewId }
            .map { it.position }
    }

    fun checkChanges(viewType: Int, viewId: Int): Flow<Pair<Int, Boolean>> {
        return checkChanges
            .filter { it.viewType == viewType && it.compoundView.id == viewId }
            .map { it.position to it.compoundView.isChecked }
    }
}
