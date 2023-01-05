package ru.tinkoff.mobile.tech.ti_recycler_coroutines.scroll

import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import ru.tinkoff.mobile.tech.ti_recycler.scroll.BaseEndLessScrollListener
import ru.tinkoff.mobile.tech.ti_recycler.scroll.DEFAULT_PAGE_SIZE
import ru.tinkoff.mobile.tech.ti_recycler.scroll.DEFAULT_THRESHOLD
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.util.checkMainThread

fun RecyclerView.endlessScrollFlow(
    pageSize: Int = DEFAULT_PAGE_SIZE,
    threshHold: Int = DEFAULT_THRESHOLD
): Flow<Int> = callbackFlow {
    checkMainThread()
    val listener = SimpleEndLessScrollListener(
        recycler = this@endlessScrollFlow,
        collector = { itemCount -> trySend(itemCount) },
        pageSize = pageSize,
        visibleThreshold = threshHold
    )
    addOnScrollListener(listener.scrollListener)
    awaitClose { listener.stopListen() }
}
    .conflate()


private class SimpleEndLessScrollListener(
    recycler: RecyclerView,
    private val collector: (Int) -> Unit,
    pageSize: Int,
    visibleThreshold: Int
) : BaseEndLessScrollListener(recycler, pageSize, visibleThreshold) {

    override fun loadMore(itemCount: Int) = collector(itemCount)
}
