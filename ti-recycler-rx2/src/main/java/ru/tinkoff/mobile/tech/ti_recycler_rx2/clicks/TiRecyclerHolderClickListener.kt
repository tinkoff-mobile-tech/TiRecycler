package ru.tinkoff.mobile.tech.ti_recycler_rx2.clicks

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import ru.tinkoff.mobile.tech.ti_recycler_rx2.base.BaseRxViewHolder

interface TiRecyclerHolderClickListener {
    fun accept(viewHolder: BaseRxViewHolder<*>, onClick: () -> Unit = {})
    fun accept(view: View, viewHolder: BaseRxViewHolder<*>, onClick: () -> Unit = {})
}

data class ItemClick(val viewType: Int, val position: Int, val view: View)

class TiRecyclerItemClicksObservable : Observable<ItemClick>(), TiRecyclerHolderClickListener {

    private val source: PublishRelay<ItemClick> = PublishRelay.create()

    override fun accept(viewHolder: BaseRxViewHolder<*>, onClick: () -> Unit) {
        viewHolder.itemView.run { setOnClickListener(Listener(source, viewHolder, this, onClick)) }
    }

    override fun accept(view: View, viewHolder: BaseRxViewHolder<*>, onClick: () -> Unit) {
        view.setOnClickListener(Listener(source, viewHolder, view, onClick))
    }

    override fun subscribeActual(observer: Observer<in ItemClick>) {
        source.subscribe(observer)
    }

    class Listener(
        private val source: Consumer<ItemClick>,
        private val viewHolder: BaseRxViewHolder<*>,
        private val clickedView: View,
        private val onClick: () -> Unit
    ) : View.OnClickListener {

        override fun onClick(v: View) {
            if (viewHolder.bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onClick()
                source.accept(
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

class TiRecyclerItemLongClicksObservable : Observable<ItemClick>(), TiRecyclerHolderClickListener {

    private val source: PublishRelay<ItemClick> = PublishRelay.create()

    override fun accept(viewHolder: BaseRxViewHolder<*>, onClick: () -> Unit) {
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

    override fun accept(view: View, viewHolder: BaseRxViewHolder<*>, onClick: () -> Unit) {
        view.setOnLongClickListener(Listener(source, viewHolder, view, onClick))
    }

    override fun subscribeActual(observer: Observer<in ItemClick>) {
        source.subscribe(observer)
    }

    class Listener(
        private val source: Consumer<ItemClick>,
        private val viewHolder: BaseRxViewHolder<*>,
        private val clickedView: View,
        private val onClick: () -> Unit
    ) : View.OnLongClickListener {

        override fun onLongClick(v: View): Boolean {
            return if (viewHolder.bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onClick()
                source.accept(
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
