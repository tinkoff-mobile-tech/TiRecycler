package ru.tinkoff.tirecycler.coroutines

import android.view.View
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.base.CoroutinesHolderFactory
import ru.tinkoff.tirecycler.R
import ru.tinkoff.tirecycler.items.HeaderUiViewHolder
import ru.tinkoff.tirecycler.items.MultiChoiceCheckViewHolder
import ru.tinkoff.tirecycler.items.TextUiViewHolder
import ru.tinkoff.tirecycler.items.TitleWithSubtitleViewHolder

class SampleCoroutinesTiRecyclerHolderFactory : CoroutinesHolderFactory() {

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
