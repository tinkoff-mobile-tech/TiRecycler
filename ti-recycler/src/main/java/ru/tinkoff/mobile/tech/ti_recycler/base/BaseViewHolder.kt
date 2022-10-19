package ru.tinkoff.mobile.tech.ti_recycler.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.clicks.TiRecyclerClickListener


open class BaseViewHolder<T : ViewTyped>(
    containerView: View
) : RecyclerView.ViewHolder(containerView) {

    @Suppress("LeakingThis")
    constructor(containerView: View, clicks: TiRecyclerClickListener) : this(containerView) {
        clicks.accept(viewHolder = this)
    }

    open fun bind(item: T) = Unit

    open fun bind(item: T, payload: List<Any>) = Unit

    open fun onViewAttachedToWindow() = Unit

    open fun onViewDetachedFromWindow() = Unit

    open fun unbind() = Unit
}
