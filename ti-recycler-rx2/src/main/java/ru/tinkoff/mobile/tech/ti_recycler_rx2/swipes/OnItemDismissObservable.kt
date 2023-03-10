package ru.tinkoff.mobile.tech.ti_recycler_rx2.swipes

import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import io.reactivex.functions.Consumer
import ru.tinkoff.mobile.tech.ti_recycler.swipes.ItemDismissSource
import ru.tinkoff.mobile.tech.ti_recycler.swipes.ItemDismissTouchHelperCallback
import ru.tinkoff.mobile.tech.ti_recycler.swipes.OnItemDismissListener

abstract class OnItemDismissObservable : Observable<RecyclerView.ViewHolder>(), ItemDismissSource

class OnItemDismissObservableImpl : OnItemDismissObservable() {

    private val source: PublishRelay<RecyclerView.ViewHolder> = PublishRelay.create()

    override fun addOnDismissListener(itemDismissTouchHelperCallback: ItemDismissTouchHelperCallback) {
        itemDismissTouchHelperCallback.addOnItemDismissListener(DismissListener(itemDismissTouchHelperCallback, source))
    }

    override fun subscribeActual(observer: Observer<in RecyclerView.ViewHolder>) = source.subscribe(observer)

    class DismissListener(
        private val itemDismissTouchHelperCallback: ItemDismissTouchHelperCallback,
        private val source: Consumer<RecyclerView.ViewHolder>
    ) : MainThreadDisposable(), OnItemDismissListener {

        override fun onDispose() = itemDismissTouchHelperCallback.clearListeners()

        override fun onItemDismiss(viewHolder: RecyclerView.ViewHolder) {
            if (!isDisposed) source.accept(viewHolder)
        }
    }
}
