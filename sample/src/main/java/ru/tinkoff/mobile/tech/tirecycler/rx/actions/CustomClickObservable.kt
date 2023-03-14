package ru.tinkoff.mobile.tech.tirecycler.rx.actions

import android.widget.Button
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler_rx2.actions.TiRecyclerCustomActionObservable
import ru.tinkoff.mobile.tech.tirecycler.actions.CustomClick

class CustomClickObservable : TiRecyclerCustomActionObservable<Button, CustomClick>() {

    override fun accept(holder: BaseViewHolder<*>, view: Button) {
        view.setOnClickListener { onNext(holder, CustomClick("custom click on ${holder.javaClass.simpleName}")) }
    }
}
