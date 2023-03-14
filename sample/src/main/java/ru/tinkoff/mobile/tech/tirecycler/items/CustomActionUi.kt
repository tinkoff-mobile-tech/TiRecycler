package ru.tinkoff.mobile.tech.tirecycler.items

import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.tirecycler.R

data class CustomActionUi(
    override val viewType: Int = R.layout.item_custom_action,
) : ViewTyped
