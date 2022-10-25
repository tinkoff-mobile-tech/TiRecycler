package ru.tinkoff.mobile.tech.ti_recycler_coroutines.clicks

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.clicks.ItemClick
import ru.tinkoff.mobile.tech.ti_recycler.clicks.TiRecyclerClickListener

class TiRecyclerItemClicksFlow : Flow<ItemClick>, TiRecyclerClickListener {

    private val source: MutableSharedFlow<ItemClick> = MutableSharedFlow(extraBufferCapacity = 1)

    override fun accept(viewHolder: BaseViewHolder<*>, onClick: () -> Unit) {
        viewHolder.itemView.run { setOnClickListener(Listener(source, viewHolder, this, onClick)) }
    }

    override fun accept(view: View, viewHolder: BaseViewHolder<*>, onClick: () -> Unit) {
        view.setOnClickListener(Listener(source, viewHolder, view, onClick))
    }

    override suspend fun collect(collector: FlowCollector<ItemClick>) {
        source.collect(collector)
    }

    class Listener(
        private val source: MutableSharedFlow<ItemClick>,
        private val viewHolder: BaseViewHolder<*>,
        private val clickedView: View,
        private val onClick: () -> Unit
    ) : View.OnClickListener {

        override fun onClick(v: View) {
            if (viewHolder.bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onClick()
                source.tryEmit(
                    ItemClick(
                        viewHolder.itemViewType,
                        viewHolder.bindingAdapterPosition,
                        clickedView
                    )
                )
            }
        }
    }
}
