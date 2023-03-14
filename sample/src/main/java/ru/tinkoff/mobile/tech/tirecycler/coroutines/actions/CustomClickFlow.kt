package ru.tinkoff.mobile.tech.tirecycler.coroutines.actions

import android.widget.Button
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.actions.TiRecyclerCustomActionFlow
import ru.tinkoff.mobile.tech.tirecycler.actions.CustomClick

class CustomClickFlow : TiRecyclerCustomActionFlow<Button, CustomClick>() {

    override fun accept(holder: BaseViewHolder<*>, view: Button) {
        view.setOnClickListener {
            onNext(holder, CustomClick("custom click on ${holder.javaClass.simpleName}"))
        }
    }
}
