package ru.tinkoff.mobile.tech.ti_recycler.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.HolderFactory
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped

abstract class BaseTiAdapter<T : ViewTyped, HF : HolderFactory>(val holderFactory: HF) :
    RecyclerView.Adapter<BaseViewHolder<ViewTyped>>() {

    abstract var items: List<T>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewTyped> {
        return holderFactory(parent, viewType)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<ViewTyped>, position: Int) =
        holder.bind(items[position])

    override fun onBindViewHolder(
        holder: BaseViewHolder<ViewTyped>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            holder.bind(items[position], payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder<ViewTyped>) {
        holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<ViewTyped>) {
        holder.onViewDetachedFromWindow()
    }

    fun isEmpty(): Boolean = items.isEmpty()
}
