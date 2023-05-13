package ru.tinkoff.mobile.tech.ti_recycler_coroutines.swipes

import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onCompletion
import ru.tinkoff.mobile.tech.ti_recycler.swipes.ItemDismissSource
import ru.tinkoff.mobile.tech.ti_recycler.swipes.ItemDismissTouchHelperCallback
import ru.tinkoff.mobile.tech.ti_recycler.swipes.OnItemDismissListener

interface OnItemDismissFlow : Flow<RecyclerView.ViewHolder>, ItemDismissSource

internal class OnItemDismissFlowImpl : OnItemDismissFlow {

    private val dismissListeners: MutableList<DismissListener> = mutableListOf()

    private val source: MutableSharedFlow<RecyclerView.ViewHolder> =
        MutableSharedFlow(extraBufferCapacity = 1)

    override suspend fun collect(collector: FlowCollector<RecyclerView.ViewHolder>) {
        source.onCompletion {
            dismissListeners.forEach { listener -> listener.stopListen() }
            dismissListeners.clear()
        }
            .collect(collector)
    }

    override fun addOnDismissListener(itemDismissTouchHelperCallback: ItemDismissTouchHelperCallback) {
        DismissListener(itemDismissTouchHelperCallback, source::tryEmit)
            .also { dismissListener ->
                dismissListeners.add(dismissListener)
                itemDismissTouchHelperCallback.addOnItemDismissListener(dismissListener)
            }
    }
}

private class DismissListener(
    private val itemDismissTouchHelperCallback: ItemDismissTouchHelperCallback,
    private val consumer: (RecyclerView.ViewHolder) -> Unit,
) : OnItemDismissListener {

    fun stopListen() = itemDismissTouchHelperCallback.clearListeners()

    override fun onItemDismiss(viewHolder: RecyclerView.ViewHolder) {
        consumer(viewHolder)
    }
}
