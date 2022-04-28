package ru.tinkoff.mobile.tech.ti_recycler_rx2.base

import android.view.View
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler_rx2.clicks.TiRecyclerHolderClickListener

open class BaseRxViewHolder<T : ViewTyped>(containerView: View) : BaseViewHolder<T>(containerView) {

    constructor(containerView: View, clicks: TiRecyclerHolderClickListener) : this(containerView) {
        clicks.accept(this)
    }
}
