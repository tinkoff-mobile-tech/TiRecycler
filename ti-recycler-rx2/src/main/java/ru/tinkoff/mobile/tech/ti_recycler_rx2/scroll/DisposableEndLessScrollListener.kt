package ru.tinkoff.mobile.tech.ti_recycler_rx2.scroll

import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import io.reactivex.disposables.Disposable
import ru.tinkoff.mobile.tech.ti_recycler.scroll.BaseEndLessScrollListener

internal class DisposableEndLessScrollListener(
    recycler: RecyclerView,
    pageSize: Int,
    visibleThreshold: Int,
    private val observer: Observer<in Int>,
) : BaseEndLessScrollListener(recycler, pageSize, visibleThreshold),
    Disposable {

    private val disposableDelegate: Disposable = object : MainThreadDisposable() {
        override fun onDispose() = this@DisposableEndLessScrollListener.stopListen()
    }

    override fun loadMore(itemCount: Int) = observer.onNext(itemCount)

    override fun dispose() = disposableDelegate.dispose()

    override fun isDisposed(): Boolean = disposableDelegate.isDisposed
}
