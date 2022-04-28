package ru.tinkoff.tirecycler

import android.view.View
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler_rx2.base.RxHolderFactory
import ru.tinkoff.tirecycler.items.HeaderUiViewHolder
import ru.tinkoff.tirecycler.items.MultiChoiceCheckViewHolder
import ru.tinkoff.tirecycler.items.TextUiViewHolder
import ru.tinkoff.tirecycler.items.TitleWithSubtitleViewHolder

class SampleTiRecyclerHolderFactory : RxHolderFactory() {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_header -> HeaderUiViewHolder(view, clicks)
            R.layout.item_multichoice -> MultiChoiceCheckViewHolder(view, clicks)
            R.layout.item_text -> TextUiViewHolder(view, clicks)
            R.layout.item_title_with_subtitle -> TitleWithSubtitleViewHolder(view, clicks)
            else -> null
        }
    }
}
