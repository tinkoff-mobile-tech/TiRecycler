package ru.tinkoff.mobile.tech.tirecycler.coroutines.actions

import android.view.View
import android.widget.Button
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.tirecycler.R
import ru.tinkoff.mobile.tech.tirecycler.items.CustomActionUi

class CustomActionViewHolder(
    view: View,
    customActionClick: CustomClickFlow,
) : BaseViewHolder<CustomActionUi>(view) {

    private val button: Button = view.findViewById(R.id.button)

    init {
        customActionClick.accept(this, button)
    }
}
