package ru.tinkoff.mobile.tech.ti_recycler_coroutines.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import ru.tinkoff.mobile.tech.ti_recycler.base.HolderFactory
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.actions.TiRecyclerCustomActionFlow
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.clicks.TiRecyclerCheckChangeFlow
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.clicks.TiRecyclerCheckChangeFlowImpl
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.clicks.TiRecyclerItemClicksFlow
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.clicks.TiRecyclerItemClicksFlowImpl
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.clicks.TiRecyclerItemLongClicksFlow
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.clicks.TiRecyclerItemLongClicksFlowImpl
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.swipes.OnItemDismissFlow
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.swipes.OnItemDismissFlowImpl
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
abstract class CoroutinesHolderFactory : HolderFactory {

    internal val swipesToDismiss: OnItemDismissFlow = OnItemDismissFlowImpl()
    protected open val clicks: TiRecyclerItemClicksFlow = TiRecyclerItemClicksFlowImpl()
    protected open val longClicks: TiRecyclerItemLongClicksFlow = TiRecyclerItemLongClicksFlowImpl()
    protected open val checkChanges: TiRecyclerCheckChangeFlow = TiRecyclerCheckChangeFlowImpl()
    protected open val customActions: Map<KClass<*>, TiRecyclerCustomActionFlow<*, *>> = emptyMap()

    fun clickPosition(vararg viewType: Int): Flow<Int> {
        return clicks
            .filter { it.viewType in viewType }
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

    fun swipesToDismiss(vararg viewType: Int): Flow<Int> {
        return swipesToDismiss
            .filter { it.itemViewType in viewType }
            .map { it.absoluteAdapterPosition }
    }

    fun <T : Any> customAction(viewType: Int, actionClass: KClass<T>): Flow<Pair<Int, T>> {
        return customActions.getValue(actionClass).source
            .filter { it.viewType == viewType }
            .map { it.position to it.value as T }
    }
}
