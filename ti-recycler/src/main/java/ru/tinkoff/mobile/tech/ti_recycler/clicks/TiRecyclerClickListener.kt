package ru.tinkoff.mobile.tech.ti_recycler.clicks

import android.view.View
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder

interface TiRecyclerClickListener {
    fun accept(viewHolder: BaseViewHolder<*>, onClick: () -> Unit = {})
    fun accept(view: View, viewHolder: BaseViewHolder<*>, onClick: () -> Unit = {})
}