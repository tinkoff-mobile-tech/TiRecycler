package ru.tinkoff.mobile.tech.ti_recycler.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import ru.tinkoff.mobile.tech.ti_recycler.base.HolderFactory
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped

class AsyncTiAdapter<T : ViewTyped, HF : HolderFactory>(
    holderFactory: HF,
    diffItemCallback: DiffUtil.ItemCallback<T>
) : BaseTiAdapter<T, HF>(holderFactory) {

    private val asyncListDiffer = AsyncListDiffer(this, diffItemCallback)

    override var items: List<T>
        get() = asyncListDiffer.currentList
        set(newItems) = asyncListDiffer.submitList(newItems)

    fun submitItems(items: List<T>, commitCallback: () -> Unit) {
        asyncListDiffer.submitList(items, commitCallback)
    }
}
