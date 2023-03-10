package ru.tinkoff.mobile.tech.ti_recycler_coroutines.actions

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.actions.TiRecyclerActionWrapper

abstract class TiRecyclerCustomActionFlow<V : View, T> : Flow<TiRecyclerActionWrapper<T>> {

    internal val source: MutableSharedFlow<TiRecyclerActionWrapper<T>> = MutableSharedFlow(extraBufferCapacity = 1)

    abstract fun accept(holder: BaseViewHolder<*>, view: V)

    override suspend fun collect(collector: FlowCollector<TiRecyclerActionWrapper<T>>) {
        source.collect(collector)
    }

    protected fun onNext(holder: BaseViewHolder<*>, value: T) {
        if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION) {
            source.tryEmit(TiRecyclerActionWrapper(holder.itemViewType, holder.bindingAdapterPosition, value))
        }
    }
}
