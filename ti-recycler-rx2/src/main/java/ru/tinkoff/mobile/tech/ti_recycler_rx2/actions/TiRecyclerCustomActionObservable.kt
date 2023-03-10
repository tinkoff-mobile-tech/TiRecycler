package ru.tinkoff.mobile.tech.ti_recycler_rx2.actions

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable
import io.reactivex.Observer
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.actions.TiRecyclerActionWrapper

abstract class TiRecyclerCustomActionObservable<V : View, T> : Observable<TiRecyclerActionWrapper<T>>() {

    internal val actionsRelay: Relay<TiRecyclerActionWrapper<T>> = PublishRelay.create()

    abstract fun accept(holder: BaseViewHolder<*>, view: V)

    final override fun subscribeActual(observer: Observer<in TiRecyclerActionWrapper<T>>) {
        actionsRelay.subscribe(observer)
    }

    protected fun onNext(holder: BaseViewHolder<*>, value: T) {
        if (holder.bindingAdapterPosition != RecyclerView.NO_POSITION) {
            actionsRelay.accept(TiRecyclerActionWrapper(holder.itemViewType, holder.bindingAdapterPosition, value))
        }
    }
}
