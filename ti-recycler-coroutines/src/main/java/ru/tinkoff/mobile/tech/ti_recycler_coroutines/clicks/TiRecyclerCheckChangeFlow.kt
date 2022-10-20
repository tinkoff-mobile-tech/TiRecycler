package ru.tinkoff.mobile.tech.ti_recycler_coroutines.clicks

import android.view.View
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.clicks.CheckItemChange
import ru.tinkoff.mobile.tech.ti_recycler.clicks.TiRecyclerCheckChangeListener

class TiRecyclerCheckChangeFlow : Flow<CheckItemChange>,
    TiRecyclerCheckChangeListener {

    private val source = MutableSharedFlow<CheckItemChange>()

    override fun accept(viewHolder: BaseViewHolder<*>, onCheckChanged: (Boolean) -> Unit) {
        (viewHolder.itemView as? CompoundButton)
            ?.run { setOnCheckedChangeListener(Listener(source, viewHolder, this, onCheckChanged)) }
            ?: throw UnsupportedOperationException("only children of CompoundButton supported")
    }

    override fun accept(
        view: View,
        viewHolder: BaseViewHolder<*>,
        onCheckChanged: (Boolean) -> Unit
    ) {
        (view as? CompoundButton)
            ?.run { setOnCheckedChangeListener(Listener(source, viewHolder, this, onCheckChanged)) }
            ?: throw UnsupportedOperationException("only children of CompoundButton supported")
    }

    override suspend fun collect(collector: FlowCollector<CheckItemChange>) {
        source.collect(collector)
    }

    class Listener(
        private val source: MutableSharedFlow<CheckItemChange>,
        private val viewHolder: BaseViewHolder<*>,
        private val clickedView: CompoundButton,
        private val onCheckChanged: (Boolean) -> Unit
    ) : CompoundButton.OnCheckedChangeListener {

        override fun onCheckedChanged(button: CompoundButton, isChecked: Boolean) = viewHolder.run {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onCheckChanged(isChecked)
                source.tryEmit(CheckItemChange(itemViewType, clickedView, bindingAdapterPosition))
            }
        }
    }
}
