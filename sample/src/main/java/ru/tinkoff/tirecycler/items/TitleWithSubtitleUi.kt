package ru.tinkoff.tirecycler.items

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.clicks.TiRecyclerClickListener
import ru.tinkoff.tirecycler.R
import ru.tinkoff.tirecycler.databinding.ItemTitleWithSubtitleBinding
import ru.tinkoff.tirecycler.util.setImageResourceOrGone
import ru.tinkoff.tirecycler.util.setTextIfNotNullOrGone

data class TitleWithSubtitleUi(
    val title: CharSequence,
    val subtitle: CharSequence? = null,
    @ColorInt val titleColor: Int? = null,
    @ColorInt val subtitleColor: Int? = null,
    @DrawableRes val icon: Int? = null,
    @DrawableRes val background: Int? = null,
    override val viewType: Int = R.layout.item_title_with_subtitle,
    override val uid: String = title.toString()
) : ViewTyped

class TitleWithSubtitleViewHolder(
    view: View,
    clicks: TiRecyclerClickListener
) : BaseViewHolder<TitleWithSubtitleUi>(view, clicks) {

    private val binding = ItemTitleWithSubtitleBinding.bind(view)

    override fun bind(item: TitleWithSubtitleUi) {
        item.background?.let(binding.container::setBackgroundResource)
        binding.title.text = item.title
        item.titleColor?.let(binding.title::setTextColor)
        binding.subtitle.setTextIfNotNullOrGone(item.subtitle)
        item.subtitleColor?.let(binding.subtitle::setTextColor)
        binding.icon.setImageResourceOrGone(item.icon)
    }
}
