package ru.tinkoff.mobile.tech.tirecycler.coroutines

import android.view.View
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.actions.TiRecyclerCustomActionFlow
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.base.CoroutinesHolderFactory
import ru.tinkoff.mobile.tech.tirecycler.R
import ru.tinkoff.mobile.tech.tirecycler.actions.CustomClick
import ru.tinkoff.mobile.tech.tirecycler.coroutines.actions.CustomActionViewHolder
import ru.tinkoff.mobile.tech.tirecycler.coroutines.actions.CustomClickFlow
import ru.tinkoff.mobile.tech.tirecycler.items.HeaderUiViewHolder
import ru.tinkoff.mobile.tech.tirecycler.items.MultiChoiceCheckViewHolder
import ru.tinkoff.mobile.tech.tirecycler.items.TextUiViewHolder
import ru.tinkoff.mobile.tech.tirecycler.items.TitleWithSubtitleViewHolder
import kotlin.reflect.KClass

class SampleCoroutinesTiRecyclerHolderFactory : CoroutinesHolderFactory() {

    private val customClick = CustomClickFlow()

    override val customActions: Map<KClass<*>, TiRecyclerCustomActionFlow<*, *>>
        get() = mapOf(CustomClick::class to customClick)

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_header -> HeaderUiViewHolder(view, clicks)
            R.layout.item_multichoice -> MultiChoiceCheckViewHolder(view, clicks)
            R.layout.item_text -> TextUiViewHolder(view, clicks)
            R.layout.item_title_with_subtitle -> TitleWithSubtitleViewHolder(view, clicks)
            R.layout.item_custom_action -> CustomActionViewHolder(view, customClick)
            else -> null
        }
    }
}
