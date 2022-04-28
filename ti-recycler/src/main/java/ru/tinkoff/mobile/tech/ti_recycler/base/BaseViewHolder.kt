package ru.tinkoff.mobile.tech.ti_recycler.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<T : ViewTyped>(
    containerView: View
) : RecyclerView.ViewHolder(containerView) {

    open fun bind(item: T) = Unit

    open fun bind(item: T, payload: List<Any>) = Unit

    open fun onViewAttachedToWindow() = Unit

    open fun onViewDetachedFromWindow() = Unit
}
