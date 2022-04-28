package ru.tinkoff.mobile.tech.ti_recycler.adapters

import android.annotation.SuppressLint
import ru.tinkoff.mobile.tech.ti_recycler.base.HolderFactory
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped

class SimpleTiAdapter<T : ViewTyped, HF : HolderFactory>(holderFactory: HF) :
    BaseTiAdapter<T, HF>(holderFactory) {

    private val localItems: MutableList<T> = mutableListOf()

    override var items: List<T>
        get() = localItems
        @SuppressLint("NotifyDataSetChanged")
        set(newItems) {
            localItems.clear()
            localItems.addAll(newItems)
            notifyDataSetChanged()
        }
}
