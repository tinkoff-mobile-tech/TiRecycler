package ru.tinkoff.mobile.tech.ti_recycler_rx2.clicks

import android.view.View
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.clicks.CheckItemChange
import ru.tinkoff.mobile.tech.ti_recycler.clicks.TiRecyclerCheckChangeListener


class TiRecyclerCheckChangeObservable : Observable<CheckItemChange>(),
    TiRecyclerCheckChangeListener {

    private val source: PublishRelay<CheckItemChange> = PublishRelay.create()

    override fun accept(viewHolder: BaseViewHolder<*>, onCheckChanged: (Boolean) -> Unit) {
        (viewHolder.itemView as? CompoundButton)?.run {
            setOnCheckedChangeListener(Listener(source, viewHolder, this, onCheckChanged))
        } ?: throw UnsupportedOperationException("only children of CompoundButton supported")
    }

    override fun accept(
        view: View,
        viewHolder: BaseViewHolder<*>,
        onCheckChanged: (Boolean) -> Unit
    ) {
        (view as? CompoundButton)?.run {
            setOnCheckedChangeListener(Listener(source, viewHolder, this, onCheckChanged))
        } ?: throw UnsupportedOperationException("only children of CompoundButton supported")
    }

    override fun subscribeActual(observer: Observer<in CheckItemChange>) {
        source.subscribe(observer)
    }

    class Listener(
        private val source: Consumer<CheckItemChange>,
        private val viewHolder: BaseViewHolder<*>,
        private val clickedView: CompoundButton,
        private val onCheckChanged: (Boolean) -> Unit
    ) : CompoundButton.OnCheckedChangeListener {

        override fun onCheckedChanged(button: CompoundButton, isChecked: Boolean) = viewHolder.run {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onCheckChanged(isChecked)
                source.accept(CheckItemChange(itemViewType, clickedView, bindingAdapterPosition))
            }
        }
    }
}
