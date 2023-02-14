package ru.tinkoff.mobile.tech.ti_recycler_rx2.clicks

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.clicks.ItemClick
import ru.tinkoff.mobile.tech.ti_recycler.clicks.TiRecyclerClickListener

abstract class TiRecyclerItemClicksObservable : Observable<ItemClick>(), TiRecyclerClickListener

class TiRecyclerItemClicksObservableImpl : TiRecyclerItemClicksObservable() {

    private val source: PublishRelay<ItemClick> = PublishRelay.create()

    override fun accept(viewHolder: BaseViewHolder<*>, onClick: () -> Unit) {
        viewHolder.itemView.run { setOnClickListener(Listener(source, viewHolder, this, onClick)) }
    }

    override fun accept(view: View, viewHolder: BaseViewHolder<*>, onClick: () -> Unit) {
        view.setOnClickListener(Listener(source, viewHolder, view, onClick))
    }

    override fun subscribeActual(observer: Observer<in ItemClick>) {
        source.subscribe(observer)
    }

    class Listener(
        private val source: Consumer<ItemClick>,
        private val viewHolder: BaseViewHolder<*>,
        private val clickedView: View,
        private val onClick: () -> Unit
    ) : View.OnClickListener {

        override fun onClick(v: View) = viewHolder.run {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onClick()
                source.accept(ItemClick(itemViewType, bindingAdapterPosition, clickedView))
            }
        }
    }
}
