package ru.tinkoff.mobile.tech.ti_recycler.scroll

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.requireAdapter

const val DEFAULT_PAGE_SIZE = 50
const val DEFAULT_THRESHOLD = 20

abstract class BaseEndLessScrollListener(
    private val recycler: RecyclerView,
    private val pageSize: Int,
    private val visibleThreshold: Int
) {

    val scrollListener: RecyclerView.OnScrollListener
    private val adapter: RecyclerView.Adapter<*> = recycler.requireAdapter
    private val layoutManager: LinearLayoutManager = recycler.layoutManager as LinearLayoutManager
    private val dataObserver: RecyclerView.AdapterDataObserver
    private var previousTotalItemCount = 0
    private var nextPage = false

    init {
        dataObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                previousTotalItemCount = 0
                nextPage = true
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                previousTotalItemCount += itemCount
                if (pageSize <= previousTotalItemCount && pageSize <= itemCount) {
                    nextPage = true
                }
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                if (positionStart == previousTotalItemCount && itemCount >= pageSize) {
                    nextPage = true
                }
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                if (itemCount >= pageSize) nextPage = true
                previousTotalItemCount -= itemCount
            }
        }
        adapter.registerAdapterDataObserver(dataObserver)
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) nextPage()
            }
        }
    }

    abstract fun loadMore(itemCount: Int)

    fun stopListen() {
        adapter.unregisterAdapterDataObserver(dataObserver)
        recycler.removeOnScrollListener(scrollListener)
    }

    private fun nextPage() {
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount
        if (nextPage && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            previousTotalItemCount = totalItemCount
            nextPage = false
            loadMore(totalItemCount)
        }
    }
}
