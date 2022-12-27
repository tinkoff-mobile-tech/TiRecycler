package ru.tinkoff.mobile.tech.ti_recycler.base.diff

import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.DiffUtil
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped

/**
 * Пример:
 *
 * class SomePayloadMapper : PayloadMapper<SomeItem> {
 *     override fun invoke(item: SomeItem) = SomePayload(...)
 * }
 * ...
 * val payloadMappers = SparseArrayCompat<PayloadMapper<ViewTyped>>(1).apply {
 *     put(R.layout.item_some_view, SomePayloadMapper() as PayloadMapper<ViewTyped>)
 * }
 * ...
 * ViewTypedDiffCallback(payloadMappers)
 *
 * @param payloadMappers - список мапперов для определенных viewType
 * */
open class ViewTypedDiffCallback<T : ViewTyped>(
    private val payloadMappers: SparseArrayCompat<PayloadMapper<T>>? = null
) : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.uid == newItem.uid
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.equals(newItem)
    }

    override fun getChangePayload(oldItem: T, newItem: T): Any? {
        return payloadMappers?.get(newItem.viewType)?.invoke(oldItem, newItem)
    }
}
