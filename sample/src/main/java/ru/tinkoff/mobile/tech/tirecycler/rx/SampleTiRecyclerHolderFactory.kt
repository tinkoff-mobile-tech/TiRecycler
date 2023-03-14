package ru.tinkoff.mobile.tech.tirecycler.rx

import android.view.View
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler_rx2.actions.TiRecyclerCustomActionObservable
import ru.tinkoff.mobile.tech.ti_recycler_rx2.base.RxHolderFactory
import ru.tinkoff.mobile.tech.tirecycler.R
import ru.tinkoff.mobile.tech.tirecycler.actions.CustomClick
import ru.tinkoff.mobile.tech.tirecycler.items.HeaderUiViewHolder
import ru.tinkoff.mobile.tech.tirecycler.items.MultiChoiceCheckViewHolder
import ru.tinkoff.mobile.tech.tirecycler.items.TextUiViewHolder
import ru.tinkoff.mobile.tech.tirecycler.items.TitleWithSubtitleViewHolder
import ru.tinkoff.mobile.tech.tirecycler.rx.actions.CustomActionViewHolder
import ru.tinkoff.mobile.tech.tirecycler.rx.actions.CustomClickObservable
import kotlin.reflect.KClass

class SampleTiRecyclerHolderFactory : RxHolderFactory() {

    private val customClick: CustomClickObservable = CustomClickObservable()

    override val customActions: Map<KClass<*>, TiRecyclerCustomActionObservable<*, *>>
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
