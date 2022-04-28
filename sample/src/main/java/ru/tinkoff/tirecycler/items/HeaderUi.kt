package ru.tinkoff.tirecycler.items

import android.view.View
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler_rx2.base.BaseRxViewHolder
import ru.tinkoff.mobile.tech.ti_recycler_rx2.clicks.TiRecyclerItemClicksObservable
import ru.tinkoff.tirecycler.R
import ru.tinkoff.tirecycler.databinding.ItemHeaderBinding

data class HeaderUi(
    val text: CharSequence,
    val textSizeSp: Float? = null,
    override val viewType: Int = R.layout.item_header,
    override val uid: String = text.toString()
) : ViewTyped

class HeaderUiViewHolder(
    view: View,
    clicks: TiRecyclerItemClicksObservable
) : BaseRxViewHolder<HeaderUi>(view, clicks) {

    private val binding = ItemHeaderBinding.bind(view)

    override fun bind(item: HeaderUi) {
        binding.header.text = item.text
        item.textSizeSp?.let(binding.header::setTextSize)
    }
}
