package ru.tinkoff.mobile.tech.ti_recycler.clicks

import android.view.View
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder

interface TiRecyclerCheckChangeListener {
    fun accept(viewHolder: BaseViewHolder<*>, onCheckChanged: (Boolean) -> Unit = {})
    fun accept(view: View, viewHolder: BaseViewHolder<*>, onCheckChanged: (Boolean) -> Unit = {})
}