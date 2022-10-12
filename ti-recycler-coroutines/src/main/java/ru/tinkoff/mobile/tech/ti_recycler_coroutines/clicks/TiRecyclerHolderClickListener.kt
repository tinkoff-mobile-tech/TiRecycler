package ru.tinkoff.mobile.tech.ti_recycler_coroutines.clicks

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.base.BaseCoroutinesViewHolder

interface TiRecyclerHolderClickListener {
    fun accept(viewHolder: BaseCoroutinesViewHolder<*>, onClick: () -> Unit = {})
    fun accept(view: View, viewHolder: BaseCoroutinesViewHolder<*>, onClick: () -> Unit = {})
}

data class ItemClick(val viewType: Int, val position: Int, val view: View)

class TiRecyclerItemClicksFlow : Flow<ItemClick>, TiRecyclerHolderClickListener {

    private val source: MutableSharedFlow<ItemClick> = MutableSharedFlow()

    override fun accept(viewHolder: BaseCoroutinesViewHolder<*>, onClick: () -> Unit) {
        viewHolder.itemView.run { setOnClickListener(Listener(source, viewHolder, this, onClick)) }
    }

    override fun accept(view: View, viewHolder: BaseCoroutinesViewHolder<*>, onClick: () -> Unit) {
        view.setOnClickListener(Listener(source, viewHolder, view, onClick))
    }

    override suspend fun collect(collector: FlowCollector<ItemClick>) {

    }

    class Listener(
        private val source: MutableSharedFlow<ItemClick>,
        private val viewHolder: BaseCoroutinesViewHolder<*>,
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

class TiRecyclerItemLongClicksFlow : Flow<ItemClick>, TiRecyclerHolderClickListener {

    private val source: MutableSharedFlow<ItemClick> = MutableSharedFlow()

    override fun accept(viewHolder: BaseCoroutinesViewHolder<*>, onClick: () -> Unit) {
        viewHolder.itemView.run {
            setOnLongClickListener(
                Listener(
                    source,
                    viewHolder,
                    this,
                    onClick
                )
            )
        }
    }

    override fun accept(view: View, viewHolder: BaseCoroutinesViewHolder<*>, onClick: () -> Unit) {
        view.setOnLongClickListener(Listener(source, viewHolder, view, onClick))
    }

    override suspend fun collect(collector: FlowCollector<ItemClick>) {
        source.collect(collector)
    }

    class Listener(
        private val source: MutableSharedFlow<ItemClick>,
        private val viewHolder: BaseCoroutinesViewHolder<*>,
        private val clickedView: View,
        private val onClick: () -> Unit
    ) : View.OnLongClickListener {

        override fun onLongClick(v: View): Boolean {
            return if (viewHolder.bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onClick()
                source.tryEmit(
                    ItemClick(
                        viewHolder.itemViewType,
                        viewHolder.bindingAdapterPosition,
                        clickedView
                    )
                )
                true
            } else {
                false
            }
        }
    }
}
